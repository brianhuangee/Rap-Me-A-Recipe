var SpotifyWebApi = require('spotify-web-api-node');
var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
var sleep = require('then-sleep');

module.exports = {
  player: function(songs) {
    var access_token;
    var device_id;

    songs = JSON.parse(decodeURI(songs.split("+").join("")));

    access_token = "BQASUWNfBG4jw-lq909K7roOdQaBqwHO7wtFQ6PqO0kkTDEYANhpZkANevOIwLcIxvbmyoHRcnjwTXRB-90wrzfr0RF9N3G3M8Wpkddgp1yqaCegZbnVOPXoAmcHrOnb7cdCxYz2M7Gu1tZdxoQaTjj1APHDhMkdGu_67lwuKyiIOE0m9rwNZt1eMIxOYY8hCOPpvKnIoL8UU6YW3TXO3a4aS0qn_K4H0GNrihOkFmciRH0dTQE-V9FvRaJkkM8-27ZisEP_u2x67Eiy_IXA-hBmtHvPl-ORhrDkqOlSqobi0KNP05YmChTM-jUOB1SFyg";
    device_id = "028544928f36409f9fea78fc410396306b81dc08";

    var spotifyApi = new SpotifyWebApi();

    spotifyApi.setAccessToken(access_token);

    for (var i = 0; i < songs.length; i++) {
      var element = songs[i];
      console.log(element.id);

      setSong(element.id)
      sleep(800).then(seekSong(element.start));
      var timeout = parseInt(element.end) >= 0 ? parseInt(element.end) : 500
      // sleep.msleep(2000 + timeout)
      // var waitTill = new Date(new Date().getTime() + timeout + 1500); while(waitTill > new Date()){}
      sleep(timeout + 1500)
    }

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
