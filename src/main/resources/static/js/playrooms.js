function create() {
    $.get('/playrooms/create').then(function (data) {
        document.location = data
    });
}

function clickRoom(data) {
    $.get(location.href + "/" + data.id + '/joinPlayer').then(function (dataRoom) {
        if (dataRoom[0] === "T")
            alert(dataRoom);
        else
            document.location = dataRoom;
    });
}

function viewRooms() {
    $.get('/playrooms/update').then(function (roomsView) {
        if (roomsView !== "") {
            $('#roomsList').empty();
            var element = document.getElementById('roomsList');
            var fragment = document.createDocumentFragment();
            var setId;
            var objGameRoom = JSON.parse(roomsView, function (key, value) {
                if (key === "number")
                    setId = value;
                if (key === "info") {
                    var buttonElement = document.createElement('button');
                    buttonElement.className = "btn";
                    buttonElement.type = "submit";
                    buttonElement.id = setId;
                    buttonElement.onclick = function () {
                        clickRoom(this);
                    };
                    buttonElement.textContent = value;
                    fragment.appendChild(buttonElement);
                    var brb = document.createElement('br');
                    fragment.appendChild(brb);
                    var brbr = document.createElement('br');
                    fragment.appendChild(brbr);
                }
            });

            element.appendChild(fragment);
        }
    });
}

function viewRoomsPerSec() {
    setInterval(viewRooms, 5000);
}

$(document).ready(function () {
    viewRooms();
    viewRoomsPerSec();
})