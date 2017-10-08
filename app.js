var express = require('express');
var $ = require("jquery");
var sleep = require("sleep");
var request = require('request');
var http = require('http');
var querystring = require('querystring');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var SpotifyWebApi = require('spotify-web-api-node')
var client_id = '4e9c3411c6944468b3aafb346a6f3ea0';
var client_secret = '92c4313b40d64bdabe7597eb2d00ce50';
var redirect_uri = 'http://localhost:8888/callback/';
var player = require('./testing.js');
var XMLHttpRequest = require('xmlhttprequest');

var app = express();

app.use(express.static(__dirname + '/public')).use(cookieParser()).use(bodyParser.json());

app.get('/', function(req, res) {
  var decoder = decodeURI(req.query.json);
  console.log(decoder);
  console.log(JSON.parse(decoder));
});

app.get('/input', function(req, res) {
  console.log(encodeURI(req.query.json));
  player.player(encodeURI(req.query.json));
})

console.log('Listening on 8888');
app.listen(8888);
