const id = parseId();
const HOMEWORK_SCORE = 63;
const BONUS_SCORE = 35;

let rollDicesBtn = document.getElementById("rollDicesBtn");
rollDicesBtn.onclick = function () {
    rollDices();
}

let categories = document.getElementsByClassName("category");
for (let i = 0; i < categories.length; i++) {
    if(i == 6 || i == 7 || i == 14) {
        continue;
    }
    categories[i].onclick = () => gain(i);
}

for (let i = 0; i < 5; i++) {
    document.getElementById("fixedCheckDiv" + (i + 1)).style.display = "none"
    document.getElementById("diceBtn" + (i + 1)).onclick = () => toggleFixed(i);
}

load();
let chance = 0;

function load() {
    fetch("/api/" + id + "/load", {
        method: "POST"
    })
        .then((response) => response.json())
        .then((json) => {
            chance = json.chance;
            for (let index = 0; index < 5; index++) {
                const value = json.dices[index];
                let diceImg = document.getElementById("diceImg" + (index + 1));
                diceImg.src = "/images/diceImg" + value + ".png";
            }
            console.log(json.playerScore);
            fillScoreBoard(json.playerScore, "black");
            let subTotal = 0;
            let total = 0;
            for (let i = 0; i < 6; i++) {
                subTotal += Number(categories[i].innerHTML);
            }
            total += subTotal;
            categories[6].innerHTML = subTotal;
            if (subTotal >= HOMEWORK_SCORE) {
                total += BONUS_SCORE;
                categories[7].innerHTML = BONUS_SCORE;
            }
            for (let i = 8; i < 14; i++) {
                total += Number(categories[i].innerHTML);
            }
            categories[14].innerHTML = total;
            fillScoreBoard(json.diceScore, "gray");

            showChance();
        })
}

function parseId() {
    let array = location.pathname.split("/");
    return array[array.length - 1];
}

let fixStates = [false, false, false, false, false];

function toggleFixed(index) {
    if (chance == 0) {
        return;
    }
    let fixedCheckDiv = document.getElementById("fixedCheckDiv" + (index + 1));
    if (fixedCheckDiv.style.display == "none") {
        fixedCheckDiv.style.display = "block";
    } else {
        fixedCheckDiv.style.display = "none";
    }

    fixStates[index] = !fixStates[index];
}

function setScore(score, category, color) {
    if (score < 0) return;
    let element = document.getElementById(category);
    element.innerHTML = score;
    element.style.color = color;
}

function fillScoreBoard(score, color) {
    setScore(score.aces, "ACES", color);
    setScore(score.deuces, "DEUCES", color);
    setScore(score.threes, "THREES", color);
    setScore(score.fours, "FOURS", color);
    setScore(score.fives, "FIVES", color);
    setScore(score.sixes, "SIXES", color);
    setScore(score.choice, "CHOICE", color);
    setScore(score.fourOfKind, "FOUR_OF_KIND", color);
    setScore(score.fullHouse, "FULL_HOUSE", color);
    setScore(score.smallStraight, "SMALL_STRAIGHT", color);
    setScore(score.largeStraight, "LARGE_STRAIGHT", color);
    setScore(score.yachu, "YACHU", color);
}

function rollDices() {
    if (chance >= 3) {
        alert("다 돌림");
        return;
    }

    fetch("/api/" + id + "/roll", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "fixStates": fixStates,
        }),
    })
        .then((response) => response.json())
        .then((json) => {
            chance = json.chance;
            for (let index = 0; index < 5; index++) {
                let diceImg = document.getElementById("diceImg" + (index + 1));
                diceImg.src = "/images/diceImg" + json.dices[index].value + ".png";
            }

            fillScoreBoard(json.score, "gray");
            showChance();
        });
}

function gain(index) {
    if (chance == 0) {
        alert("주사위를 굴리십시오");
        return;
    }

    let element = categories[index];
    const category = element.id;
    const score = element.innerHTML;

    categories[index].onclick = null;

    fetch("/api/" + id + "/gain", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "category": category,
            "gained": Number(score),
        }),
    }).then(() => {
        element.style.color = "black";
        chance = 0;
        fixStates = [false, false, false, false, false];
        for (let i = 0; i < 5; i++) {
            document.getElementById("fixedCheckDiv" + (i + 1)).style.display = "none";
        }
        for (let index = 0; index < 5; index++) {
            let diceImg = document.getElementById("diceImg" + (index + 1));
            diceImg.src = "/images/diceImg0.png";
        }
        for (let index = 0; index < categories.length; index++) {
            if (categories[index].style.color == "gray") {
                categories[index].innerHTML = "";
            }
        }

        let total = categories[categories.length - 1];
        total.innerHTML = Number(total.innerHTML) + Number(score);

        if (!isHomework(index)) {
            return;
        }

        let subTotal = categories[6];
        subTotal.innerHTML = Number(subTotal.innerHTML) + Number(score);

        if (!hasBonusScore() && isSatisfiedHomework(subTotal)) {
            categories[7].innerHTML = BONUS_SCORE;
            total.innerHTML = Number(total.innerHTML) + BONUS_SCORE;
        }

        showChance();

    })

    function isHomework(index) {
        return index >= 0 && index <= 5;
    }

    function hasBonusScore() {
        return categories[7].innerHTML == BONUS_SCORE;
    }

    function isSatisfiedHomework(subTotal) {
        return subTotal.innerHTML >= HOMEWORK_SCORE;
    }
}

function textFileLoad() {
    let content = null;
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "/text/gameRule.txt", false);
    xmlhttp.send();
    if (xmlhttp.status == 200) {
        content = xmlhttp.responseText;
        content = content.replace(/\r\n/ig, '<br>');
        content = content.replace(/\r/ig, '<br>');
        content = content.replace(/\n/ig, '<br>');
    }
    document.getElementById("gameRuleContent").innerHTML = content;

    xmlhttp.open("GET", "/text/genealogy.txt", false);
    xmlhttp.send();
    if (xmlhttp.status == 200) {
        content = xmlhttp.responseText;
        content = content.replace(/\r\n/ig, '<br>');
        content = content.replace(/\r/ig, '<br>');
        content = content.replace(/\n/ig, '<br>');
    }
    document.getElementById("genealogyContent").innerHTML = content;
}

textFileLoad();

function openLayerPopup(popupContent) {
    document.getElementById("layerPopup").style.display = "flex";
    document.getElementById(popupContent).style.display = "block";
}

function closeLayerPopup() {
    var popupContents = document.getElementsByClassName("layerPopupContent");
    for(var element of popupContents) {
        element.style.display = "none";
    }
    document.getElementById("layerPopup").style.display = "none";
}

function showChance() {
    document.getElementById("chanceText").innerHTML = "남은 횟수 : " + (3 - chance);
}
