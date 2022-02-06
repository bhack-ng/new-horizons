var path;
var realtyObjectId;
var floorSchemeId;

// Запрос данных.
window.onload = function() {

    // Получение параметров.
    path = document.getElementById('pageJS').src;
    path = path.replace('client.js', '');
    realtyObjectId = new URL(window.location.href).searchParams.get("realtyObjectId");
    floorSchemeId = new URL(window.location.href).searchParams.get("floorSchemeId");

    // Запуск скрипта.
    var script = document.createElement("script");
    script.src = path + "../show-offices/data.js?floorSchemeId=" + floorSchemeId;
    document.head.appendChild(script);
};

// Инициализация.
function back(json) {
    // Параметры.
    var items = json.items;
    var points = [];
    var canvas = document.getElementById("myCanvas");
    var ctx = canvas.getContext("2d");

    // Отрисовка фонового изображения.
    var img = new Image();
    img.onload = function() {
        render(ctx, img, points, items);
    }
    img.src = path + "../show-offices/image?imageId=" + json.imageId;

    //
    updateButtons(points);

    canvas.onclick = function(e) {
        // Сокрытие окна.
        hidePopup();

        // Получение позиции мыши.
        var pos = getCanvasMousePos(canvas, e);

        // Добавление точки.
        points.push([pos.x, pos.y]);

        // Перерисовка.
        render(ctx, img, points, items);

        // Активация / деактивация кнопки.
        updateButtons(points);
     };

    canvas.oncontextmenu = function(e) {
        // Получение позиции мыши.
        var pos = getCanvasMousePos(canvas, e);

        // Проверка на нажатие на полигон.
        if (pointInPoly(pos, points)) {
            showPopup(pos, e);
            return false;
        }
     };

     // Очистка.
     var btnClear = document.getElementById("clear");
     btnClear.onclick = function(e) {
        points = [];

        hidePopup();

        render(ctx, img, points, items);

        updateButtons(points);
     };

     // Сохранение.
     var saveButtons = document.getElementsByClassName("save");
     for (let i = 0; i < saveButtons.length; i++) {
        saveButtons[i].onclick = function(e) {
                // Отправка запроса.
                var xsr = new XMLHttpRequest();
                var url = path + "save";
                xsr.open("POST", url, false);
                xsr.send( [realtyObjectId, floorSchemeId, points] );

                // Перезагрузка страницы.
                location.reload();
             };
     }
};

// Перерисовка.
function render(ctx, img, points, items) {
    // Отрисовка изображения.
    ctx.globalAlpha = 1;
    ctx.drawImage(img, 0, 0);

    // Отрисовка других полигонов.
    drawItems(ctx, items, "#009933", "#ff6666");

    // Отрисовка пользовательского полигона.
    if (points.length > 0) {
        // Отрисовка точек.
        drawPoints(ctx, points, "#FF0000");

        // Отрисовка полигона.
        drawPolygon(ctx, points, "#e7d74b");
    }
};

// Отрисовка элементов.
function drawItems(ctx, items, freeColor, busyColor) {
    for(let i = 0; i < items.length; i++) {
        if (items[i].coords.length > 0) {

            // Выбор цвета.
            var color = (items[i].free) ? freeColor : busyColor;

            // Отрисовка.
            drawPolygon(ctx, items[i].coords, color);
        }
    }
};

// Отрисовка точек.
function drawPoints(ctx, points, color) {
    ctx.fillStyle = color;
    var radius = 3;

    for (let i = 0; i < points.length; i++) {
        ctx.beginPath();
        ctx.arc(points[i][0], points[i][1], 3, 0, 2 * Math.PI);
        ctx.fill();
    }
};

// Отрисовка полигона.
function drawPolygon(ctx, points, color) {

    ctx.fillStyle = color;
    ctx.beginPath();
    ctx.moveTo(points[0][0], points[0][1]);
    for (let i = 0; i < points.length; i++) {
        ctx.lineTo(points[i][0], points[i][1]);
    }
    ctx.closePath();
    ctx.stroke();

    ctx.globalAlpha = 0.6;
    ctx.fill();
};

// Получение позиции мыши.
function getCanvasMousePos(canvas, evt) {
        var rect = canvas.getBoundingClientRect();
        return {
          x: evt.clientX - rect.left,
          y: evt.clientY - rect.top
        };
};

// Проверка наличия точки в многоугольнике.
// ray-casting algorithm based on
// http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
function pointInPoly(point, vs) {
    // Параметры.
    var x = point.x;
    var y = point.y;

    // Проверка.
    var inside = false;
    for (var i = 0, j = vs.length - 1; i < vs.length; j = i++) {
        var xi = vs[i][0], yi = vs[i][1];
        var xj = vs[j][0], yj = vs[j][1];

        var intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }

    return inside;
};

// Показ окна с информацией.
function showPopup(pos, evt) {
    var div = document.getElementById("popup");

    // Настройка позиции окна.
    div.style.display = "block";
    var divInfo = div.getBoundingClientRect();
    div.style.left = evt.clientX - divInfo.width / 2 + "px";
    div.style.top = evt.clientY - divInfo.height - 10  + "px";
};

// Сокрытие окна.
function hidePopup() {
    var div = document.getElementById("popup");
    div.style.display = "none";
};

// Активация / дактивация кнопок.
function updateButtons(points) {
    var saveButtons = document.getElementsByClassName("save");
        for (let i = 0; i < saveButtons.length; i++) {
            saveButtons[i].style.background = points.length < 3 ? "grey" : "green";
            saveButtons[i].disabled = points.length < 3;
        }
};