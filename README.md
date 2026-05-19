# Five-Card Draw Poker v2 — Modular, No Build Tools

Modular rewrite of the original poker app. Same gameplay, same JavaFX UI —
split into focused single-responsibility classes. No Maven, no Gradle.

---

## Project Structure

```
poker-v2/
├── src/
│   └── poker/
│       ├── Card.java              # Immutable card (Suit + Rank enums)
│       ├── Deck.java              # 52-card ArrayList, shuffle + deal
│       ├── Hand.java              # 5 cards + full hand evaluator
│       ├── Player.java            # Chips, high score, hand counters
│       ├── GameEngine.java        # State machine — zero JavaFX imports
│       ├── StatsManager.java      # File I/O: reads/writes stats.txt
│       └── ui/
│           ├── PokerApp.java      # Main app — wires panels to engine only
│           ├── CardView.java      # Single card widget + flip animation
│           ├── HeaderBar.java     # Top strip: title, chips, high score
│           ├── GameArea.java      # Card row, result label, instructions
│           ├── PaytablePanel.java # Left sidebar: rankings + row highlight
│           ├── StatsPanel.java    # Right sidebar: hands, win rate
│           ├── ControlBar.java    # Bottom: bet slider + Deal/Draw/Reset
│           ├── UiFactory.java     # Shared button/label/background factory
│           └── PokerStyleSheet.java # All CSS as a data-URI string
├── .vscode/
│   ├── tasks.json                 # Ctrl+Shift+B → compile + run
│   └── settings.json              # Set javafx.lib path here
├── run.sh                         # Linux / macOS one-shot script
├── run.bat                        # Windows one-shot script
└── README.md
```

---

### Logic layer

| Class          | Does                                       |
| -------------- | ------------------------------------------ |
| `Card`         | Value object — Suit/Rank enums, immutable  |
| `Deck`         | Shuffle + deal from ArrayList              |
| `Hand`         | Evaluate all 9 hand ranks via Java Streams |
| `Player`       | Chip balance, high score, win counters     |
| `GameEngine`   | IDLE → DEALT → RESULT state machine        |
| `StatsManager` | Read/write stats.txt in key=value format   |

### UI layer (JavaFX)

| Class             | Does                                                         |
| ----------------- | ------------------------------------------------------------ |
| `PokerApp`        | Assembles panels, wires callbacks, calls `refreshAll()`      |
| `CardView`        | Face-up/down card, HOLD toggle, flip animation               |
| `HeaderBar`       | Title + chip/high-score labels                               |
| `GameArea`        | Card row, deal/draw animations, result + flash messages      |
| `PaytablePanel`   | Paytable rows, highlights winning rank                       |
| `StatsPanel`      | Hands played, win rate                                       |
| `ControlBar`      | Bet slider, Deal/Draw/New Game buttons, enable/disable state |
| `UiFactory`       | Shared factory: buttons, labels, spacers, felt background    |
| `PokerStyleSheet` | All CSS in one string, returned as a data-URI                |

---

## How to Run

### Requirements

- **JDK 17+**
- **JavaFX SDK 17+**

###

```bat
set JAVAFX_HOME=C:\javafx-sdk-21\lib
run.bat
```

### VS Code

1. Open this folder in VS Code
2. Edit `.vscode/settings.json` → set `"javafx.lib"` to your SDK's `lib` path
3. Press **Ctrl+Shift+B** → choose **2 – Run Poker**

---

## Gameplay

1. Set your bet with the slider (min 10, max 200, capped to your chips)
2. **DEAL** — 5 cards animate in face-up
3. Click cards to **HOLD** them (card lifts + gold glow)
4. **DRAW** — un-held cards are replaced; hand is evaluated
5. Winning hands highlight in the paytable; chips update automatically
6. Stats auto-saved to `stats.txt` after every hand
7. **NEW GAME** — resets the round; going broke refills to 1000 chips

## Paytable

| Hand            | Payout |
| --------------- | ------ |
| Royal Flush     | 250×   |
| Straight Flush  | 50×    |
| Four of a Kind  | 25×    |
| Full House      | 9×     |
| Flush           | 6×     |
| Straight        | 4×     |
| Three of a Kind | 3×     |
| Two Pair        | 2×     |
| One Pair        | 1×     |
| High Card       | —      |
