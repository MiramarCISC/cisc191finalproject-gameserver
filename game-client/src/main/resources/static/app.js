const apiBaseUrl = "/web/matches";

let currentMatchId = null;
let currentPlayerName = "Player";

const playerNameInput = document.querySelector("#playerNameInput");
const difficultySelect = document.querySelector("#difficultySelect");
const rankedCheckbox = document.querySelector("#rankedCheckbox");

const matchIdSpan = document.querySelector("#matchId");
const playerNameSpan = document.querySelector("#playerName");
const opponentNameSpan = document.querySelector("#opponentName");
const winnerNameSpan = document.querySelector("#winnerName");
const log = document.querySelector("#log");

document.querySelector("#joinButton").addEventListener("click", joinMatch);
document.querySelector("#playButton").addEventListener("click", playMatch);
document.querySelector("#historyButton").addEventListener("click", loadHistory);
document.querySelector("#resetButton").addEventListener("click", resetLocalView);

function getPlayerName() {
    const typedName = playerNameInput.value.trim();
    return typedName.length === 0 ? "Player" : typedName;
}

async function joinMatch() {
    currentPlayerName = getPlayerName();

    const request = {
        playerName: currentPlayerName,
        difficulty: difficultySelect.value,
        ranked: rankedCheckbox.checked
    };

    appendLog("Joining match through web client REST facade...");

    try {
        const response = await fetch(apiBaseUrl, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(request)
        });

        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Could not join match.");

        currentMatchId = data.matchId;
        matchIdSpan.textContent = data.matchId;
        playerNameSpan.textContent = data.playerName;
        opponentNameSpan.textContent = data.opponentName;
        winnerNameSpan.textContent = "TBD";

        appendLog(data.message);
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

async function playMatch() {
    if (!currentMatchId) {
        appendLog("Join a match before playing.");
        return;
    }

    appendLog("Server is choosing a random winner...");

    try {
        const response = await fetch(`${apiBaseUrl}/${currentMatchId}/play?playerName=${encodeURIComponent(currentPlayerName)}`, {
            method: "POST"
        });

        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Could not play match.");

        winnerNameSpan.textContent = data.winnerName;
        appendLog(data.message);
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

async function loadHistory() {
    const playerName = encodeURIComponent(getPlayerName());
    appendLog("Loading persisted match history through gRPC...");

    try {
        const response = await fetch(`${apiBaseUrl}/history?playerName=${playerName}`);
        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Could not load history.");

        appendLog("Match history:");
        data.matches.forEach(match => appendLog("- " + match));
    } catch (error) {
        appendLog("Error: " + error.message);
    }
}

function resetLocalView() {
    currentMatchId = null;
    currentPlayerName = "Player";
    matchIdSpan.textContent = "None";
    playerNameSpan.textContent = "Player";
    opponentNameSpan.textContent = "Opponent";
    winnerNameSpan.textContent = "TBD";
    log.textContent = "";
    appendLog("Local view reset.");
}

function appendLog(message) {
    log.textContent += message + "\n";
}
