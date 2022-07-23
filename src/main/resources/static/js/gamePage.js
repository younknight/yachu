const id = parseId();

let rollDicesBtn = document.getElementById("rollDicesBtn");
rollDicesBtn.onclick = function () {
    rollDices();
}

let categories = document.getElementsByClassName("category");
for (let i = 0; i < categories.length; i++) {
    categories[i].onclick = () => gain(i);
}

function gain(index) {
    if (chance == 0) {
        alert("주사위를 굴리십시오")
        return;
    }

    let element = categories[index];
    const category = element.id;
    const score = element.innerHTML;

    fetch("/api/" + id + "/gain", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "category": category,
            "score": parseInt(score),
        }),
    }).then(() => {
        element.style.color = "black"
        chance = 0;
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

        if (index >= 0 && index <= 5) {
            let subTotal = categories[6];
            subTotal.innerHTML = Number(subTotal.innerHTML) + Number(score);

            if (categories[7].innerHTML != 35) {
                if (subTotal.innerHTML >= 63) {
                    categories[7].innerHTML = "35";
                    total.innerHTML = Number(total.innerHTML) + 35;
                }
            }
        }
    })
}

for (let i = 0; i < 5; i++) {
    document.getElementById("fixedCheckDiv" + (i + 1)).style.display = "none"
    document.getElementById("diceBtn" + (i + 1)).onclick = () => toggleFixed(i);
}

let chance = 0;

function parseId() {
    let array = window.location.pathname.split("/");
    return array[array.length - 1];
}

function toggleFixed(index) {
    if(chance == 0) {
        return;
    }
    let fixedCheckDiv = document.getElementById("fixedCheckDiv" + (index + 1));
    if (fixedCheckDiv.style.display == "none") {
        fixedCheckDiv.style.display = "block";
    } else {
        fixedCheckDiv.style.display = "none";
    }
    fetch("/api/" + id + "/toggle/" + index, {
        method: "POST",
    });
}

function rollDices() {
    if (chance >= 300) {
        alert("다 돌림");
        return;
    }

    fetch("/api/" + id + "/roll", {
        method: "POST",
    })
        .then((response) => response.json())
        .then((json) => {
            for (let index = 0; index < 5; index++) {
                let diceImg = document.getElementById("diceImg" + (index + 1));
                diceImg.src = "/images/diceImg" + json.dices[index].value + ".png";
            }

            if (json.score.aces >= 0) {
                let element = document.getElementById("ACES");
                element.innerHTML = json.score.aces;
                element.style.color = "gray";
            }
            if (json.score.deuces >= 0) {
                let element = document.getElementById("DEUCES");
                element.innerHTML = json.score.deuces;
                element.style.color = "gray";
            }
            if (json.score.threes >= 0) {
                let element = document.getElementById("THREES");
                element.innerHTML = json.score.threes;
                element.style.color = "gray";
            }
            if (json.score.fours >= 0) {
                let element = document.getElementById("FOURS");
                element.innerHTML = json.score.fours;
                element.style.color = "gray";
            }
            if (json.score.fives >= 0) {
                let element = document.getElementById("FIVES");
                element.innerHTML = json.score.fives;
                element.style.color = "gray";
            }
            if (json.score.sixes >= 0) {
                let element = document.getElementById("SIXES");
                element.innerHTML = json.score.sixes;
                element.style.color = "gray";
            }
            if (json.score.choice >= 0) {
                let element = document.getElementById("CHOICE");
                element.innerHTML = json.score.choice;
                element.style.color = "gray";
            }
            if (json.score.fourOfKind >= 0) {
                let element = document.getElementById("FOUR_OF_KIND");
                element.innerHTML = json.score.fourOfKind;
                element.style.color = "gray";
            }
            if (json.score.fullHouse >= 0) {
                let element = document.getElementById("FULL_HOUSE");
                element.innerHTML = json.score.fullHouse;
                element.style.color = "gray";
            }
            if (json.score.smallStraight >= 0) {
                let element = document.getElementById("SMALL_STRAIGHT");
                element.innerHTML = json.score.smallStraight;
                element.style.color = "gray";
            }
            if (json.score.largeStraight >= 0) {
                let element = document.getElementById("LARGE_STRAIGHT")
                element.innerHTML = json.score.largeStraight;
                element.style.color = "gray";
            }
            if (json.score.yachu >= 0) {
                let element = document.getElementById("YACHU")
                element.innerHTML = json.score.yachu;
                element.style.color = "gray";
            }
        });
    chance++;
}