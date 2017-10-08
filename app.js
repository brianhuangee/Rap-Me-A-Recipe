var express = require('express');
var $ = require("jquery");
var sleep = require("sleep");
var request = require('request');
var querystring = require('querystring');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var SpotifyWebApi = require('spotify-web-api-node')
var client_id = '4e9c3411c6944468b3aafb346a6f3ea0';
var client_secret = '92c4313b40d64bdabe7597eb2d00ce50';
var redirect_uri = 'http://localhost:8888/callback/';

var stateKey = 'spotify_auth_state';

var app = express();

var songs = "";

var generateRandomString = function(length) {
  var text = '';
  var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

  for (var i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }
  return text;
};
app.use(express.static(__dirname + '/public')).use(cookieParser()).use(bodyParser.json());

app.get('/login', function(req, res) {

  var state = generateRandomString(16);
  res.cookie(stateKey, state);

  // your application requests authorization
  var scope = 'user-read-private user-read-email user-read-playback-state';
  res.redirect('https://accounts.spotify.com/authorize?' +
    querystring.stringify({
      response_type: 'code',
      client_id: client_id,
      scope: scope,
      redirect_uri: redirect_uri,
      state: state
    }));
});

app.get('/callback', function(req, res) {
  var code = req.query.code || null;
  var state = req.query.state || null;
  var storedState = req.cookies ? req.cookies[stateKey] : null;

  if (state === null || state !== storedState) {
    res.redirect('/#' +
      querystring.stringify({
        error: 'state_mismatch'
      }));
  } else {
    res.clearCookie(stateKey);
    var authOptions = {
      url: 'https://accounts.spotify.com/api/token',
      form: {
        code: code,
        redirect_uri: redirect_uri,
        grant_type: 'authorization_code'
      },
      headers: {
        'Authorization': 'Basic ' + (new Buffer(client_id + ':' + client_secret).toString('base64'))
      },
      json: true
    };

    request.post(authOptions, function(error, response, body) {
      if (!error && response.statusCode === 200) {
        var access_token = body.access_token,
            refresh_token = body.refresh_token;

        var activeDevice;

        var options = {
          url: 'https://api.spotify.com/v1/me/player/devices',
          headers: { 'Authorization': 'Bearer ' + access_token },
          json: true
        };

        // use the access token to access the Spotify Web API
        request.get(options, function(error, response, body) {
          body.devices.forEach(function(element) {
            if (element.is_active == true) {
              activeDevice = element;

              res.redirect('/#' +
                querystring.stringify({
                  access_token: access_token,
                  refresh_token: refresh_token,
                  device_id: activeDevice.id
                }));
            }
          });
        });

} else {
        res.redirect('/login' +
          querystring.stringify({
            error: 'invalid_token'
          }));
      }
    });
  }
});
function runThis() {
  console.log("running fuck")
  access_token = "BQAm-J0knYOs5AzSetr-ntogkU8JTD3MS33rlXUfMv5qTx0KM-oLPzHfaLJ-YhnccKC3Lk6lL14vlh9LIxQ8D2DTt6rP3Bo1jf5Q_7bYkMsyhsEGuqdBJcpFrJI14BHP12USV4z9C90dk385AZLi_1k_dx-hsPY0c5RKN0GYSby5EFRnTtRQh5reQhi4RJbe1_r5gE67IZUD0s3gm1Xi11QlpljnfFToecLXn4K_nhuepnWwc2M57z3O4kdeeeiM_f1r5EGXNbb8WvJw9TM4Wp4PwWlviveyiKTZfZ48T5Ljp1Of40fiI4Sell7tQsj5sQ";
  device_id = "028544928f36409f9fea78fc410396306b81dc08";

  var spotifyApi = new SpotifyWebApi(
    {
      clientId : "4e9c3411c6944468b3aafb346a6f3ea0",
      clientSecret : "92c4313b40d64bdabe7597eb2d00ce50"
    }
  );
  console.log("running fuck2")

  spotifyApi.setAccessToken(access_token);

  for (var i = 0; i < songs.length; i++) {
    var element = songs[i];
    console.log(element.id);

    setSong(element.id, access_token, device_id)
    sleep.msleep(800)
    seekSong(element.start, access_token, device_id)
    var timeout = parseInt(element.end) >= 0 ? parseInt(element.end) : 500
    sleep.msleep(2000 + timeout)
  }
  console.log("running fuck3")

  spotifyApi.pause({"device_id": device_id});
};
function setSong(id, access_token, device_id) {
  console.log(id)
  var options = {
    url: "https://api.spotify.com/v1/me/player/play?device_id=" + device_id,
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + access_token
    },
    method : 'PUT',
    json: '{"uris": ["spotify:track:' + id + '"]}'
  };
  request(options, callbackFunc(error, response, body))
}

function callbackFunc(error, response, body) {
  console.log(response.statusCode);
  console.log(response.statusMessage);
}

function seekSong(time, access_token, device_id) {
  console.log(time)
  var options = {
    url: 'https://api.spotify.com/v1/me/player/seek?position_ms=' + time + '&device_id=' + device_id,
    method: "PUT",
    headers: {
      "Authorization": "Bearer " + access_token
    }
  }
  request(options, callbackFunc(error, response, body))
}

app.post('/input', function(req, res) {
  songs = req.body;
  console.log("Reqing that bod")
  console.log(songs);
  runThis();
});

app.get('/refresh_token', function(req, res) {

  // requesting access token from refresh token
  var refresh_token = req.query.refresh_token;
  var authOptions = {
    url: 'https://accounts.spotify.com/api/token',
    headers: { 'Authorization': 'Basic ' + (new Buffer(client_id + ':' + client_secret).toString('base64')) },
    form: {
      grant_type: 'refresh_token',
      refresh_token: refresh_token
    },
    json: true
  };

  request.post(authOptions, function(error, response, body) {
    if (!error && response.statusCode === 200) {
      var access_token = body.access_token;
      res.send({
        'access_token': access_token
      });
    }
  });
});

console.log('Listening on 8888');
app.listen(8888);
