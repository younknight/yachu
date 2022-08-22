const id = parseId();

let rollDicesBtn = document.getElementById("rollDicesBtn");
rollDicesBtn.onclick = function () {
    rollDices();
}

let categories = document.getElementsByClassName("category");
for (let i = 0; i < categories.length; i++) {
    if (i == 6 || i == 7 || i == 14) {
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
            fillScoreBoard(json.playerScore, "black");
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

function setScore(category, color) {
    if (!category.acquired) return;
    let element = document.getElementById(category.genealogy);
    element.innerHTML = category.point;
    element.style.color = color;
}

function fillScoreBoard(score, color) {
    for (let i = 0; i < score.categories.length; i++) {
        let category = score.categories[i];
        setScore(category, color);
    }
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
    })
        .then((response) => response.json())
        .then((json) => {
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

            setScore(json.score.categories[index], "black");
            setScore(json.score.categories[6], "black");
            setScore(json.score.categories[7], "black");
            setScore(json.score.categories[14], "black");

            if (json.over) {
                openRecordPopup(json.score.categories[14].point);
            }

            showChance();

        })

    function openRecordPopup(recordScore) {
        openLayerPopup("recordContent");
        document.getElementById("popUpCloseBtn").style.display = "none";
        document.getElementById("rollDicesBtn").onclick = null;
        document.getElementById("recordScore").innerHTML = "Score : " + recordScore;
        document.getElementById("recordBtn").onclick = () => recordRanking();
    }

    function recordRanking() {
        const recordNickname = document.getElementById("recordNickname").value;
        const recordScore = Number(categories[14].innerHTML);
        fetch("/api/record/new", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "nickname": recordNickname,
                "score": recordScore,
            }),
        })
            .then((response) => response.json())
            .then((json) => {
                loadRanking(json);
                openRankingPopup();
            })
    }

    function openRankingPopup() {
        openLayerPopup("rankingContent");
        document.getElementById("recordContent").style.display = "none";
    }

    function loadRanking(rankingId) {
        fetch("/api/record", {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            },
        }).then((response) => response.json())
            .then((json) => {
                for (let ranking = 0; ranking < 10; ranking++) {
                    let tmpTableRow = document.createElement("tr");
                    if (json[ranking].id == rankingId) {
                        tmpTableRow.style.color = "green";
                    }

                    let tmpRankingNum = document.createElement("td");
                    tmpRankingNum.innerHTML = ranking + 1;
                    tmpTableRow.appendChild(tmpRankingNum);

                    let tmpRankingNickName = document.createElement("td");
                    let tmpRankingScore = document.createElement("td");
                    if (ranking < json.length) {
                        tmpRankingNickName.innerHTML = json[ranking].nickname;
                        tmpRankingScore.innerHTML = json[ranking].score;
                    }
                    tmpTableRow.appendChild(tmpRankingNickName);
                    tmpTableRow.appendChild(tmpRankingScore);

                    document.getElementById("rankingTableBody").appendChild(tmpTableRow);
                }
            });
    }
}

function textFileLoad() {
    fetch("/text/gameRule.txt")
        .then((res) => res.text())
        .then((data) => {
            data = data.replace(/\r\n/ig, '<br>');
            data = data.replace(/\r/ig, '<br>');
            data = data.replace(/\n/ig, '<br>');
            document.getElementById('gameRuleContent').innerHTML = data;
        })
}

textFileLoad();

function openLayerPopup(popupContent) {
    document.getElementById("layerPopup").style.display = "flex";
    document.getElementById(popupContent).style.display = "flex";
}

function closeLayerPopup() {
    var popupContents = document.getElementsByClassName("layerPopupContent");
    for (var element of popupContents) {
        element.style.display = "none";
    }
    document.getElementById("layerPopup").style.display = "none";
}

function showChance() {
    document.getElementById("chanceText").innerHTML = "남은 횟수 : " + (3 - chance);
}
