
ClientJS.callback = function() {
    // Параметры.
    var combobox = document.getElementsByClassName("combo")[0];

    ClientJS.freeColor = "#FF00FF";
    ClientJS.busyColor = "#555511";

    // Добавление элементов в Select.
    for (let i = 0; i < ClientJS.floors.length; i++) {
        var option = document.createElement("option");
        option.text = ClientJS.floors[i].imageName;
        option.value = i;
        combobox.add(option);
    }

    // Выбор элемента combobox.
    combobox.onchange = function (evt) {
        ClientJS.change(evt.target.value);
    };

    // Выбор первого элемента.
    if (ClientJS.floors.length > 0) {
        combobox.selectedIndex = 0;
        ClientJS.change(0);
    }
};