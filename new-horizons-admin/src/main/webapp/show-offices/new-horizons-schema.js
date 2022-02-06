// Глобальный объект с данными.
var ClientJS = {};

// Список предметов для текущего плана.
ClientJS.items = null;

// Список всех планов.
ClientJS.floors = null;

// Метод, вызываемый после загрузки данных.
ClientJS.callback = null;

// Путь к javascript файлу.
ClientJS.path = null;

// Цвет свободного объекта.
ClientJS.freeColor = "#009933";

// Цвет занятого объекта.
ClientJS.busyColor = "#ff6666";

// Запрос данных.
window.addEventListener("load", addJs, false);

// Вызов загрузки.
function addJs () {
    ClientJS.path = document.getElementById('clientJS').src;
    ClientJS.path = ClientJS.path.replace('new-horizons-schema.js','');

    // Запуск скрипта.
    var script = document.createElement("script");
    script.src = ClientJS.path + "data.js";
    document.head.appendChild(script);
};

// Callback для загрузки JSON.
function back(json) {

    // Параметры.
    ClientJS.canvas = document.getElementsByClassName("canvas")[0];
    ClientJS.img = new Image();
    ClientJS.floors = json;

    // Насройка канваса.
    ClientJS.canvas.onmousemove = function (evt) {
            if (ClientJS.items != null) {
                // Получение позиции мыши.
                var pos = getCanvasMousePos(ClientJS.canvas, evt);

                // Поиск элемента под мышью.
                findMouseElement(pos, ClientJS.items);

                // Перерисовка.
                render(ClientJS.canvas, ClientJS.img, ClientJS.items);
            }
        };

    ClientJS.canvas.onclick = function (evt) {
            if (ClientJS.items != null) {
                // Получение позиции мыши.
                var pos = getCanvasMousePos(ClientJS.canvas, evt);

                // Показ окна.
                if (ClientJS.mouse != null) showPopup(ClientJS.canvas, ClientJS.mouse, evt);
                else hidePopup();
            }
        };

     // Callback.
     ClientJS.callback();
};

// Выбор элемента.
ClientJS.change = function (item) {

    // Параметры.
    var floors = ClientJS.floors;
    var img = ClientJS.img;
    var canvas = ClientJS.canvas;

    // Получение объектов.
    ClientJS.items = floors[item].items;

    // Получение изображения.
    img.src = ClientJS.path + "image?imageId=" + floors[item].imageId;
    img.onload = function() {
        // Перерисовка.
        render(canvas, img, ClientJS.items);
     };
};

// Отрисовка содержимого.
function render(canvas, img, items) {
    // Очистка.
    var ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);

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
            ctx.globalAlpha = (items[i] == ClientJS.mouse) ? 0.8 : 0.6;

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
    var coords = item.coords;

    // Выбор цвета.
    ctx.fillStyle = (item.free) ? ClientJS.freeColor : ClientJS.busyColor;

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
// Алгоритм трассировки лучей основан на:
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