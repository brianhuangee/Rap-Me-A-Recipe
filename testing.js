var SpotifyWebApi = require('spotify-web-api-node');
var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
var sleep = require('then-sleep');

module.exports = {
  player: function(songs) {
    var access_token;
    var device_id;

    songs = JSON.parse(decodeURI(songs.split("+").join("")));

    access_token = "BQAe6GY3A-ygdMkAJESqVun2G6GRVAgn5clSiX3fuov5IEQkAf9VVRfq9f8rD-JIVTK4nCFjpfWU46SNFXThUNVXH0wAIeIJ3Dh9nINV5tfAOWmA8mvSnK6wL6_XuGNuju0fzYL7iWupaz4znLZr1WwG-xo5uvlUHIMN4iIKF_8xL6yX3P7r67D8U9UZLZBFXlnVnCKfXlYt2fstT0wf4sC-D9s8BW9V-M6Ru-M4Zt8tHbRmD-SJr9MsCA64kxGlLO_N5iKlwBVSzCBWOZGu-uue0hdKS8eCsO4Ky_2YHuqNVVYvQvZL05sGnxAGHoUtHw";
    device_id = "028544928f36409f9fea78fc410396306b81dc08";

    var spotifyApi = new SpotifyWebApi();

    spotifyApi.setAccessToken(access_token);

      (function looper(ind) {
        var element = songs[ind];
        console.log(element.id);
        setTimeout(setSong(element.id), 1200);
        seekSong(element.start);
        if (ind < songs.length) setTimeout(function() {
          looper(++ind);
        }, 2000);
      })(0);

    spotifyApi.pause({"device_id": device_id});

    function setSong(id) {
      var xmlhttp = new XMLHttpRequest();
   xmlhttp.open("PUT","https://api.spotify.com/v1/me/player/play?device_id=" + device_id, true);
   xmlhttp.setRequestHeader("Authorization", "Bearer " + access_token);
   xmlhttp.setRequestHeader("Content-Type", "application/json");
   xmlhttp.send('{"uris": ["spotify:track:' + id + '"]}');

    }

    function seekSong(time) {
     var xmlhttp = new XMLHttpRequest();
   xmlhttp.open("PUT","https://api.spotify.com/v1/me/player/seek?position_ms=" + time + '&device_id=' + device_id, true);
   xmlhttp.setRequestHeader("Authorization", "Bearer " + access_token);
   xmlhttp.send();
  }
  }
  };
