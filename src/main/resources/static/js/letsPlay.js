function deleteRoom() {
    $.get(location.href + '/delete').then(function (data) {
        document.location = data
    });
}

function leaveRoom() {
    $.get(location.href + '/leave').then(function (data) {
        document.location = data
    });
}

function clickHim(data) {
    $.get(location.href + '/click_to_user/' + data.id).then(function (addToChat) {
        if (addToChat[0] === "<") {
            var temp = document.getElementById("textMessage").value;
            document.getElementById("textMessage").value = temp + " " + addToChat + " ";
        } else if (addToChat[0] === "Y")
            alert(addToChat);
        else if (addToChat[0] === " ")
            alert("❤ Прошлой ночью дама выбрала именно тебя ❤");
        console.log(data.id)
    });
}

function game() {
    $.get(location.href + '/game').then(function (jsonGameRooms) {
        if (jsonGameRooms === "") {
            console.log("game stop");
            letsPlay(1);
            startAgain(0);
        } else {
            $("#info").empty();
            $("#roleInfo").empty();
            var elementInfo = document.getElementById('info');
            var fragmentInfo = document.createDocumentFragment();
            var elementRole = document.getElementById('roleInfo');
            var fragmentRole = document.createDocumentFragment();
            var objGameRoom = JSON.parse(jsonGameRooms, function (key, value) {
                if (key === "timer") {
                    var messageElementInfo = document.createElement('a');
                    messageElementInfo.textContent = value;
                    fragmentInfo.appendChild(messageElementInfo);
                } else if (key === "role") {
                    var messageElementRole = document.createElement('a');
                    messageElementRole.style = "font-size: 5vh; font-family: revert";
                    messageElementRole.textContent = value;
                    fragmentRole.appendChild(messageElementRole);
                }
            });
            elementInfo.appendChild(fragmentInfo);
            elementRole.appendChild(fragmentRole);
        }
    });
}

var intervalGame;

function letsPlay(data) {
    console.log("letsPlay " + data);
    if (data === 0)
        intervalGame = setInterval(game, 1000);
    if (data === 1)
        clearInterval(intervalGame);
}

var interval;

function startCheck() {
    $.get(location.href + '/start').then(function (data) {
            console.log("st Check");
            if (data !== -2 && data !== -3 && data !== 0)
                $("#info").html("Время до начала: " + data);
            else if (data === 0) {
                $("#info").html("Регистрация...");
            } else if (data === -3) {
                document.location = "/playrooms";
                alert("Владелец комнаты удалил ее :(");
            } else if (data === -2) {
                console.log("start");
                startAgain(1);
                letsPlay(0);
            }
        }
    )
}

function startAgain(data) {
    console.log("startAgain " + data);
    if (data === 0)
        interval = setInterval(startCheck, 1000);
    if (data === 1)
        clearInterval(interval);
}

function sendMessage() {
    $.get(location.href + '/chat/' + document.getElementById("textMessage").value).then(function (jsonGameRooms) {
        $('#chat').empty();
        document.getElementById("textMessage").value = "";
        if (jsonGameRooms !== "" && jsonGameRooms !== "nope") {
            var element = document.getElementById('chat');
            var fragment = document.createDocumentFragment();
            var objGameRoom = JSON.parse(jsonGameRooms, function (key, value) {
                if (key === "message") {
                    var messageElement = document.createElement('a');
                    messageElement.textContent = value;
                    fragment.appendChild(messageElement);
                    var brb = document.createElement('br');
                    fragment.appendChild(brb);
                }
            });
            element.appendChild(fragment);
        } else if (jsonGameRooms === "") {
            alert("🔫 Ночью в чате общается только мафия 🔫‍");
        } else {
            alert("╋ Пожалуй, в следующей жизни ╋‍");
        }
    });
}

function chatUpdate() {
    $.get(location.href + '/chat').then(function (jsonGameRooms) {
        $('#chat').empty();
        if (jsonGameRooms !== "") {
            var element = document.getElementById('chat');
            var fragment = document.createDocumentFragment();
            var objGameRoom = JSON.parse(jsonGameRooms, function (key, value) {
                if (key === "message") {
                    var messageElement = document.createElement('a');
                    messageElement.textContent = value;
                    fragment.appendChild(messageElement);
                    var brb = document.createElement('br');
                    fragment.appendChild(brb);
                }
            });
            element.appendChild(fragment);
        }
    });

}

function resChat() {
    setInterval(chatUpdate, 1000);
}

function viewUsers() {
    $.get(location.href + '/update_view_players').then(function (jsonGameRooms) {
        $('#usersList').empty();
        var element = document.getElementById('usersList');
        var fragment = document.createDocumentFragment();
        var classString = "";
        var objGameRoom = JSON.parse(jsonGameRooms, function (key, value) {
            if (key === "username") {
                classString = value;

            }
            if (key === "password") {
                var buttonElement = document.createElement('button');
                buttonElement.type = "submit";
                buttonElement.className = value;
                buttonElement.id = classString;
                buttonElement.onclick = function () {
                    clickHim(this);
                };
                buttonElement.textContent = classString;
                fragment.appendChild(buttonElement);
                // var brb = document.createElement('br');
                // fragment.appendChild(brb);
            }
        });
        element.appendChild(fragment);
    });

}

function buttonDelRoom() {
    $.get(location.href + '/btnCheck').then(function (data) {
            if (data === 0) {
                $('#forHost').empty();
            }
        }
    )
}

function viewUsersPerSec() {
    setInterval(viewUsers, 1000);
}

$(document).ready(function () {
    viewUsers();
    chatUpdate();
    viewUsersPerSec();
    buttonDelRoom();
    startAgain(0);
    startCheck();
    resChat();
})