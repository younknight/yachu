const id = parseId();
let chance = 0;
let fixStates = [false, false, false, false, false];
let categories = document.getElementsByClassName("category");
let tmpScoreBoard = new Map();

window.onload = function () {
    loadGamePage();
}

function parseId() {
    let array = location.pathname.split("/");
    return array[array.length - 1];
}

function loadGamePage() {
    loadDice();
    loadGameState();
    loadTextFile();
    addBtnFunction();
}

function addBtnFunction() {
    document.getElementById("rollDicesBtn").onclick = () => rollDices();
    document.getElementById("gameRuleShowBtn").onclick = () => openLayerPopup('gameRuleContent');
    document.getElementById("popUpCloseBtn").onclick = () => closeLayerPopup();
    document.getElementById("anima").onclick = () => settingAnima();
    document.getElementById("nextBtn").onclick = () => nextText();
    document.getElementById("prevBtn").onclick = () => prevText();

    for (let i = 0; i < categories.length; i++) {
        if (i == 6 || i == 7 || i == 14) {
            continue;
        }
        categories[i].onclick = () => gain(i);
    }
}

function loadDice() {
    for (let i = 0; i < 5; i++) {
        let tmpDiceBtn = document.createElement("button");
        tmpDiceBtn.className = "diceBtn";
        tmpDiceBtn.id = "diceBtn" + (i + 1);
        tmpDiceBtn.onclick = () => toggleFixed(i);

        let tmpDiceImg = document.createElement("img");
        tmpDiceImg.src = "/images/diceImg0.png";
        tmpDiceImg.id = "diceImg" + (i + 1);
        tmpDiceImg.alt = "diceImg";
        tmpDiceBtn.appendChild(tmpDiceImg);

        let tmpFixedCheckDiv = document.createElement("div");
        tmpFixedCheckDiv.className = "fixedCheckDiv";
        tmpFixedCheckDiv.id = "fixedCheckDiv" + (i + 1);
        tmpFixedCheckDiv.style.display = "none";
        tmpDiceBtn.appendChild(tmpFixedCheckDiv);

        document.getElementById("diceBtnContainer").appendChild(tmpDiceBtn);
    }
}

function loadGameState() {
    fetch("/api/" + id + "/load", {
        method: "POST"
    })
        .then((response) => response.json())
        .then((json) => {
            setGameState(json.chance, json.dices, json.playerScore, "black");
            fillScoreBoard(json.diceScore, "gray");
            setTmpScoreBoard(json.diceScore);
        })
}

let rule = 0;
let text=[];
let ment=[];
function loadTextFile() {
    fetch("/text/gameRule.txt")
        .then((res) => res.text())
        .then((data) => {
            text=data.split('@');
            showText();
        })
    fetch("/text/ment.txt")
        .then((res) => res.text())
        .then((data) => {
            ment=data.split('@');
            console.log(ment);
        })
}

function showText(){
    document.getElementById('Rule').innerHTML = text[rule];
    document.getElementById('RuleIndex').innerHTML = (rule + 1) + " / " + text.length;
}

function nextText(){
    ++rule;
    if(rule==text.length) rule=0;
    showText();
}

function prevText(){
    --rule;
    if(rule==-1) rule=text.length-1;
    showText();
}
let time = 0;
let maxCnt = 5;
let delay = 150;

function settingAnima(){
    if(delay!=0) {
        delay=0;
    }
    else {
        delay=150;
    }
}
function rollDices() {
    if (chance >= 3) {
        openLayerPopup('chanceOut');
        document.getElementById("ment").innerHTML = ment[Math.floor(Math.random() * ment.length)];
        return;
    }

    const target = document.getElementById('rollDicesBtn');
    target.disabled = true;

    clearInterval(time);
    time = setInterval("throwDice()",delay);
    setTimeout(() => fetch("/api/" + id + "/roll", {
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
            let diceStates = [];
            for (let index = 0; index < 5; index++) {
                diceStates.push(json.dices[index].value);
            }
            setGameState(json.chance, diceStates, json.score, "gray");
            setTmpScoreBoard(json.score)
            clearInterval(time);
            target.disabled = false;
        }), delay * maxCnt);
}
function throwDice() {
    for (let value = 0; value < 5; value++) {
        console.log(value + fixStates[value])
        if(!fixStates[value]) {
            setDiceImg(value, Math.floor(Math.random() * 6) + 1);
        }
    }
}
function gain(index) {
    if (chance == 0) {
        alert("주사위를 굴리십시오");
        return;
    }

    let element = categories[index];
    const category = element.id;
    const score = tmpScoreBoard.get(category);

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
            let diceStates = [0, 0, 0, 0, 0];
            fixStates = [false, false, false, false, false];
            for (let i = 0; i < 5; i++) {
                document.getElementById("fixedCheckDiv" + (i + 1)).style.display = "none";
            }
            setGameState(0, diceStates, json.score, "black");
            tmpScoreBoard.clear();
            for (let index = 0; index < categories.length; index++) {
                if (categories[index].style.color == "gray") {
                    categories[index].innerHTML = "0";
                }
            }

            if (json.over) {
                openRecordPopup(json.score.categories[14].point);
            }
        })

    function openRecordPopup(recordScore) {
        openLayerPopup("recordContent");
        document.getElementById("popUpCloseBtn").style.display = "none";
        document.getElementById("rollDicesBtn").onclick = null;
        document.getElementById("recordScore").innerHTML = "Score : " + recordScore;
        document.getElementById("recordBtn").onclick = () => recordRanking(recordScore);
    }

    function recordRanking(recordScore) {
        const recordNickname = document.getElementById("recordNickname").value;
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

                    let tmpRankingNum = document.createElement("td");
                    tmpRankingNum.innerHTML = ranking + 1;
                    tmpTableRow.appendChild(tmpRankingNum);

                    let tmpRankingNickName = document.createElement("td");
                    let tmpRankingScore = document.createElement("td");
                    if (ranking < json.length) {
                        if (json[ranking].id == rankingId) {
                            tmpTableRow.style.color = "green";
                        }
                        tmpRankingNickName.innerHTML = json[ranking].nickname;
                        tmpRankingScore.innerHTML = json[ranking].score;
                    }
                    tmpTableRow.appendChild(tmpRankingNickName);
                    tmpTableRow.appendChild(tmpRankingScore);

                    document.getElementById("rankingTableBody").appendChild(tmpTableRow);
                }

                let restartBtn = document.createElement("a");
                restartBtn.innerHTML = "RESTART";
                restartBtn.href = "/";
                document.getElementById("rankingContent").appendChild(restartBtn);
            });
    }
}

function setTmpScoreBoard(scoreState) {
    for(let index = 0; index < scoreState.categories.length; index++) {
        tmpScoreBoard.set(scoreState.categories[index].genealogy, scoreState.categories[index].point);
    }
}

function setGameState(chanceState, diceStates, scoreState, scoreColor) {
    chance = chanceState;
    for (let index = 0; index < 5; index++) {
        setDiceImg(index, diceStates[index]);
    }
    fillScoreBoard(scoreState, scoreColor);
    showChance();
}

function fillScoreBoard(score, color) {
    for (let i = 0; i < score.categories.length; i++) {
        let category = score.categories[i];
        setScore(category, color);
    }
}

function setScore(category, color) {
    if (!category.acquired) return;
    let element = document.getElementById(category.genealogy);
    element.innerHTML = category.point;
    element.style.color = color;
    if (color == "black") {
        element.onclick = null;
    }
}

function setDiceImg(index, value) {
    let diceImg = document.getElementById("diceImg" + (index + 1));
    diceImg.src = "/images/diceImg" + value + ".png";
}

function showChance() {
    document.getElementById("chanceText").innerHTML = "남은 횟수 : " + (3 - chance);
}

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

function openLayerPopup(popupContent) {
    document.getElementById("layerPopup").style.display = "flex";
    document.getElementById(popupContent).style.display = "flex";
}

function closeLayerPopup() {
    let popupContents = document.getElementsByClassName("layerPopupContent");
    for (let element of popupContents) {
        element.style.display = "none";
    }
    document.getElementById("layerPopup").style.display = "none";
}