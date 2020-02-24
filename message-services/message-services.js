/*
Héctor Granja Cortés
2º DAM - PSP
Unit 3 - Final Exercise
*/

const mongoose = require('mongoose');
const express = require('express');
const jwt = require('jsonwebtoken');
const sha256 = require('sha256');
const fs = require('fs');
const bodyParser = require('body-parser');
const moment = require('moment');

mongoose.Promise = global.Promise;

//DB Connection / Creation

mongoose.connect('mongodb://localhost:27017/users');

//Schemes

let userSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true,
        trim: true,
        minlength: 1
    },
    password: {
        type: String,
        required: true,
        min: 4
    },
    image: {
        type: String,
        required: true
    }
});

let messageSchema = new mongoose.Schema({
    from: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users',
        required: true
    },
    to: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'users',
        required: true
    },
    message: {
        type: String,
        required: true,
        trim: true,
        minlength: 1
    },
    image: {
        type: String,
        required: true
    },
    sent: {
        type: String,
        required: true,
        trim: true,
        minlength: 10
    }
});


//Tables

let User = mongoose.model('users', userSchema);
let Message = mongoose.model('messages', messageSchema);


//Global Variables

const secretWord = "DAMsecret";


//Launching services

let app = express();
app.use(bodyParser.json());

app.use(express.static('public'));
app.use('/img', express.static(__dirname + '/img'));

postLogin();
postRegister();
getUsers();
putUsers();
getMessages();
postMessages();
deleteMessages();

app.listen(8080);


//Auth and auxiliary functions

let generateToken = id => {
    let token = jwt.sign({ _id: id }, secretWord,
        { expiresIn: "2 years" });
    return token;
}

let validateToken = token => {
    try {
        token = token.substring(7);
        let result = jwt.verify(token, secretWord);
        return result;
    }
    catch (e) {
    }
}

let decodeImage = image => {
    let filePath = `img/${Date.now()}.jpg`;
    fs.writeFileSync(filePath, Buffer.from(image, 'base64'));
    return filePath;
}


//Services

function postLogin() {
    app.post('/login', (req, res) => {
        let result;
        let userClient = {
            name: req.body.name,
            password: sha256(req.body.password)
        };
        User.find({
            name: userClient.name,
            password: userClient.password
        }).then(data => {
            if (data.length != 0) {

                result = {
                    ok: true,
                    token: generateToken(data[0]._id),
                    name: data[0].name,
                    image: data[0].image
                }

            } else {
                result = {
                    ok: false,
                    error: "User or password incorrect"
                };
            }
        }).catch(error => {
            result = {
                ok: false,
                error: "Error validating user"
            };
        }).finally(send => {
            res.send(result)
        });
    });
}

function postRegister() {
    app.post('/register', (req, res) => {

        let newUser = new User({
            name: req.body.name,
            password: sha256(req.body.password),
            image: decodeImage(req.body.image)
        });

        newUser.save().then(result => {
            let data = {
                ok: true
            };
            res.send(data);

        }).catch(error => {
            let data = {
                ok: false,
                error: "User couldn't be registered"
            };
            res.send(data);
        });
    });
}

function getUsers() {
    app.get('/users', (req, res) => {
        let token = req.headers['authorization'];
        let result = validateToken(token);
        if (result) {
            User.find().then(result => {
                let data = {
                    ok: true,
                    users: result
                };
                res.send(data);
            })
        } else {
            res.sendStatus(401)
        }
    });
}


function putUsers() {
    app.put('/users', (req, res) => {
        let data;
        let token = req.headers['authorization'];
        let result = validateToken(token);
        if (result) {
            User.findById(result._id).then(user => {
                user.image = decodeImage(req.body.image)
                user.save().then(save => {
                    data = {
                        ok: true
                    }
                    res.send(data);
                }).catch(error => {
                    data = {
                        ok: false,
                        error: "Error updating user: " + result._id
                    }
                    res.send(data);
                });
            }).catch(error => {
                data = {
                    ok: false,
                    error: "User: " + result._id + " not found"
                }
                res.send(data);
            });

        } else {
            res.sendStatus(401);
        }
    });
}

function getMessages() {
    app.get('/messages', (req, res) => {
        let data;
        let token = req.headers['authorization'];
        let result = validateToken(token);
        if (result) {
            Message.find({ to: result._id }).populate('from').then(messages => {
                data = {
                    ok: true,
                    messages: messages
                }
            }).catch(error => {
                data = {
                    ok: false,
                    error: "Error getting messages for user: " + result._id
                }
            }).finally(send => {
                res.send(data);
            })
        } else {
            res.sendStatus(401);
        }
    });
}

function postMessages() {
    app.post('/messages/:toUserId', (req, res) => {
        let token = req.headers['authorization'];
        let result = validateToken(token);
        if (result) {
            let newMessage = new Message(
                {
                    from: result._id,
                    to: req.params.toUserId,
                    message: req.body.message,
                    image: decodeImage(req.body.image),
                    sent: new moment().format("DD/MM/YYYY")
                });
            newMessage.save().then(result => {
                let data = {
                    ok: true
                };
                res.send(data);
            }).catch(error => {
                let data = {
                    ok: false,
                    error: "Error sending a message to: " + req.params.toUserId
                };
                res.send(data);
            });
        } else {
            res.sendStatus(401);
        }
    });
}

function deleteMessages() {
    app.delete('/messages/:id', (req, res) => {
        let token = req.headers['authorization'];
        let result = validateToken(token);
        if(result){
        Message.findById(req.params.id).then(message => {
            message.remove().then(remove => {
                data = {
                    ok: true,
                }
                res.send(data);
            }).catch(error => {
                data = {
                    ok: false,
                    error: "Error deleting message: " + req.params.id
                }
                res.send(data);
            });
        }).catch(error => {
            data = {
                ok: false,
                error: "Message: " + req.params.id + " not found"
            }
            res.send(data);
        });
        } else {
            res.sendStatus(401);
        }
    });
}
