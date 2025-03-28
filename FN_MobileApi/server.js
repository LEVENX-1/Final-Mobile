var express = require('express');
var app = express();
const bcrypt = require('bcrypt');
var mysql = require('mysql');
const util = require('util');
require('dotenv').config();
const cors = require('cors');
const { log } = require('console');

// 🛠 การตั้งค่า middleware
app.use(express.json());  // ใช้ express.json() แทน body-parser
app.use(cors());  // เปิดใช้งาน CORS เพื่อให้สามารถเรียก API ได้จากที่อื่น

// 📦 การเชื่อมต่อฐานข้อมูล MySQL
var dbConn = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    database: process.env.DB_NAME,
});

// ตรวจสอบการเชื่อมต่อฐานข้อมูล
dbConn.connect(function (err) {
    if (err) {
        console.error('Error connecting to database:', err.stack);
        return;
    }
    console.log('Connected to database as id ' + dbConn.threadId);
});

const query = util.promisify(dbConn.query).bind(dbConn); // เปลี่ยน query ให้ใช้งาน async/await ได้สะดวก

// ⬇️ โมดูลนี้จะถูกส่งออกไปใช้ในโปรเจคอื่นๆ ได้
module.exports = app;
app.use(express.json());

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// 📚 GET: ดึงข้อมูลออเดอร์ทั้งหมด
app.get('/AllOrder', async function (req, res) {
    try {
        const results = await query('SELECT * FROM customer_order');
        res.send(results);
    } catch (error) {
        console.error(error);
        res.status(500).send({ message: 'Database query failed', error });
    }
});


// 📚 GET: ดึงข้อมูลออเดอร์โดยใช้ name, size, number, topping และเลือก id ล่าสุด
app.get('/OneOrder', async function (req, res) {
    log(req.query);
    try {
        const { id } = req.query;
        
        if (!id) {
            return res.status(400).send({ message: 'ID is required' });
        }

        // คำสั่ง SQL สำหรับค้นหาออเดอร์โดยใช้ id
        const queryStr = 'SELECT * FROM customer_order WHERE id = ? ';
        const results = await query(queryStr, [id]);

        // Log ข้อมูลที่ดึงออกมาจากฐานข้อมูล
        // console.log('Order data retrieved:', results.length > 0 ? results[0] : 'No matching order found');

        // ส่งข้อมูลออเดอร์กลับไป
        res.send(results.length > 0 ? results[0] : { message: 'No matching order found' });
    } catch (error) {
        console.error(error);
        res.status(500).send({ message: 'Database query failed', error });
    }
});


// 📌 API สำหรับเพิ่มออเดอร์และคืนค่า ID ที่เพิ่มลง Database
app.post('/InsertOrder', function (req, res) {
    const { name, size, number, topping, sweetness, price } = req.body;
    console.log(req.body); // ตรวจสอบข้อมูลที่ได้รับ

    // ตรวจสอบข้อมูลก่อนบันทึก
    if (!name || !size || !number || !topping || !sweetness || !price) {
        return res.status(400).send({ message: 'Missing required fields' });
    }

    if (isNaN(number) || number <= 0) {
        return res.status(400).send({ message: 'Number must be a positive integer' });
    }

    if (isNaN(price) || price <= 0) {
        return res.status(400).send({ message: 'Price must be a positive number' });
    }

    // คำสั่ง SQL สำหรับเพิ่มข้อมูล
    const insertQuery = 'INSERT INTO customer_order (name, size, number, topping, sweetness, price) VALUES (?, ?, ?, ?, ?, ?)';
    
    dbConn.query(insertQuery, [name, size, number, topping, sweetness, price], function (error, results) {
        if (error) {
            console.error(error);
            return res.status(500).send({ message: 'Database query failed', error });
        }

        // ดึง ID ของออเดอร์ที่เพิ่งเพิ่ม
        const insertedId = results.insertId;

        res.status(201).send({
            message: 'Order inserted successfully',
            id: insertedId // ส่งค่า ID กลับไปให้ Client
        });
    });
});

// ///////////////////////////////////////////// เริ่มต้นเซิร์ฟเวอร์ ///////////////////////////////////////////////
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});

// การจัดการข้อผิดพลาดทั่วไป (Error Handling)
app.use((err, req, res, next) => {
    console.error(err);
    res.status(500).send({ message: 'Something went wrong!' });
});
