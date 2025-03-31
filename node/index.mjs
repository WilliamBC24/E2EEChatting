import express from 'express';
import i18n from 'i18n';
import path from 'path';
import { fileURLToPath } from 'url';
import session from 'express-session';
import cors from 'cors';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

i18n.configure({
    locales: ['en', 'vi', 'ko'],
    directory: path.join(__dirname, '/src/locales'),
    defaultLocale: 'en',
    cookie: 'lang',
    queryParameter: 'lang',
    objectNotation: true,
});

const app = express();
//To provide css and js
app.use(express.static('public'));
app.use(i18n.init);
app.use(cors({
    origin: "http://localhost:3000",
    credentials: true
}));
app.use(session({
    secret: 'secret',
    cookie: { maxAge: 60000 },
    saveUninitialized: false,
    cookie: { secure: false },
    resave: false
}))
app.set('view engine', 'ejs');


app.get('/login', (req, res) => {
    res.render('login', { lang: res.__ });
});

app.get('/register', (req, res) => {
    res.render('register', { lang: res.__})
})

app.get('/chat', (req, res) => {
    res.render('chat', { lang: res.__})
})

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

