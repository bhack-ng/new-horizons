// Глобальный объект.
var ClientJS = {};

// Элемент на который наведена мышь.
ClientJS.mouse = null;

// Путь к JS файлу.
ClientJS.path = null;

// Запрос данных.
window.onload = function() {

    // Получение параметров.
    ClientJS.path = document.getElementById('pageJS').src;
    ClientJS.path = ClientJS.path.replace(new RegExp('new-horizons-schema.js$'), '');

    // Формирование строки и Id схем.
    var data = "";
    var canvases = document.getElementsByClassName("floor");
    for (let i = 0; i < canvases.length; i++) {
            data += canvases[i].getAttribute("data-floorId") + "|";
        }
    data = data.slice(0, -1);

    // Запрос JSON.
    var script = document.createElement("script");
    script.src = ClientJS.path + "../show-offices/data.js?floorSchemeId=" + data;
    document.head.appendChild(script);
};

// Callback для загрузки JSON.
function back(json) {

    // Получение элементов.
    var canvases = document.getElementsByClassName("floor");

    // Инициализация canvas'ов.
    if (typeof json.length === 'undefined') {
        initCanvas(canvases[0], json);
    } else {
        for (let i = 0; i < json.length; i++) {
            if (typeof json[i].imageId != 'undefined') {
                initCanvas(canvases[i], json[i]);
            }
        }
    }
};

// Инициализация canvas.
function initCanvas(canvas, data) {
    var ctx = canvas.getContext("2d");

    // Инициализация фонового изображения.
    var img = new Image();
    img.src = ClientJS.path + "../show-offices/image?imageId=" + data.imageId;
    img.onload = function() {
            // Отрисовка.
            render(ctx, img, data.items);
    };

    // Наведение мыши на canvas.
    canvas.onmousemove = function (e) {
            // Получение позиции мыши.
            var pos = getCanvasMousePos(canvas, e);

            // Поиск элемента под мышью.
            findMouseElement(pos, data.items);

            // Перерисовка.
            render(ctx, img, data.items);
        };

    // Клик по canvas.
    canvas.onclick = function (e) {
            // Получение позиции мыши.
            var pos = getCanvasMousePos(canvas, e);

            // Показ окна.
            if (ClientJS.mouse != null) showPopup(canvas, ClientJS.mouse, e);
            else hidePopup();
        };
};

// Отрисовка содержимого.
function render(ctx, img, items) {
    // Отрисовка изображения.
    ctx.globalAlpha = 1;
    ctx.drawImage(img, 0, 0);

    // Отрисовка элементов.
    drawItems(ctx, items);
};

// Отрисовка элементов.
function drawItems(ctx, items) {
    for (let i = 0; i < items.length; i++) {
        if (items[i].coords.length > 0) {

            // Проверка на наведение мышью.
            ctx.globalAlpha = (items[i] == ClientJS.mouse)? 0.8 : 0.6;

            // Отрисовка элемента.
            drawItem(ctx, items[i]);
        }
    }
};

// Поиск элемента на который наведена мышь.
function findMouseElement(pos, items) {
    ClientJS.mouse = null;
    for (let i = 0; i < items.length; i++) {
        if (items[i].coords.length > 0) {
            // Проверка позиции курсора.
            if (pointInPoly(pos, items[i].coords)) {
                ClientJS.mouse = items[i];
            }
        }
    }
};

// Рисование элемента.
function drawItem(ctx, item) {
    // Параметры.
    var freeColor = "#009933";
    var busyColor = "#ff6666";
    var coords = item.coords;

    // Выбор цвета.
    ctx.fillStyle = (item.free) ? freeColor : busyColor;

    // Отрисовка.
    ctx.beginPath();
    ctx.moveTo(...coords[0]);
    for (let i = 1; i < coords.length; i++) {
        ctx.lineTo(...coords[i]);
    }
    ctx.closePath();
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
// Алгоритм трассировки лучей основанный на:
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
function showPopup(canvas, item, evt) {
    var popup = document.getElementsByClassName("new-horizons-popup")[0];

    // Настройка позиции окна.
    popup.style.display = "block";
    var divInfo = popup.getBoundingClientRect();
    popup.style.left = evt.clientX - divInfo.width / 2  + "px";
    popup.style.top = evt.clientY - divInfo.height - 10 + "px";

    // Вывод изображения.
    var image = popup.getElementsByClassName("realty-image")[0];
    if (item.photos.length > 0) {
        image.src = ClientJS.path + "../photo?id=" + item.photos[0];
    } else {
        image.src = ClientJS.path + "No Image.png";
    }

    // Получение элементов.
    var number = popup.getElementsByClassName("realty-number")[0];
    var status = popup.getElementsByClassName("realty-status")[0];
    var area = popup.getElementsByClassName("realty-area")[0];
    var offerText = popup.getElementsByClassName("realty-offer-text")[0];

    // Вывод информации.
    var numberString = (item.number === "") ? "Нет данных" : item.number;
    number.innerHTML = "Офис № " + numberString;
    var areaString = (item.area === "") ? "Нет данных" : item.area + " м<sup>2</sup>";
    area.innerHTML = "Площадь: " + areaString;
    offerText.innerHTML = (item.offerText == "") ? "Нет текста объявления" : item.offerText;
    status.innerHTML = (item.free) ? "Свободно" : "Занято";
};

// Сокрытие окна.
function hidePopup() {
    var popup = document.getElementsByClassName("new-horizons-popup")[0];
    popup.style.display = "none";
};