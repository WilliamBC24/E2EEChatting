import express from 'express';
import path from 'path';

const app = express();
app.set('view engine', 'ejs');

const users = [
    { id: 1, name: 'John' },
    { id: 2, name: 'Doe' },
    { id: 3, name: 'Jane' },
];

app.get('/login', (req, res) => {
    res.render('login');
});

app.get('/', (req, res) => {
    res.send('Hello World!');
});

app.get('/test', (req, res) => {
    res.send('we up!');
});

app.post('/users', (req, res) => {
    res.send('User created');
});

app.listen(3000, () => {
    console.log('Server is running on port 3000');
});