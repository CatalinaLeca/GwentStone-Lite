# **GwentStone Lite**
Autor: **Leca Maria Cătălina - 315CA**  
Copyright © 2023-2024    
An universitar: **2023-2024**  
Materia: **Programarea Orientată pe Obiecte**  
Tema 0 - **GwentStone Lite - Joc de Cărți în Java**   

--- 


## 📖 **Descrierea Proiectului**
Acest proiect implementează un **joc de cărți bazat pe turnuri**, inspirat de **Hearthstone** și **Gwent**, folosind **programare orientată pe obiecte (OOP) în Java**. Jocul este destinat a fi jucat de **doi jucători**, fiecare având un **deck de cărți** și un **erou** cu abilități speciale.

Jocul folosește un **AI** pentru a simula cei doi jucători, iar serverul generează **comenzi de debugging** pentru verificarea statusului jocului și colectarea **statisticilor despre partide**.

### 🔹 **Obiective**
- **Familiarizarea cu Java și conceptele POO** (moștenire, agregare, polimorfism).
- **Dezvoltarea unui cod modular și extensibil**, ușor de modificat pentru adăugarea de noi funcționalități.
- **Respectarea principiilor de organizare OOP**, cu separarea clară a responsabilităților între clase.
- **Implementarea mecanicilor de joc**, cum ar fi atacurile, utilizarea abilităților speciale și gestionarea manei.

---

## ⚙️ **Funcționalități principale**
Programul implementează un set de **mecanici de joc** și **comenzi pentru gestionarea jocului**.

### 🔹 **Gameplay**
- **Joc bazat pe ture**: Fiecare jucător își desfășoară acțiunile pe rând.
- **Sistem de mana**: Jucătorii primesc mana la începutul fiecărei runde, necesară pentru a juca cărți.
- **Atac și apărare**: Minionii pot ataca cărțile inamice sau eroul adversarului.
- **Câștigarea jocului**: Un jucător câștigă atunci când **eroul adversar rămâne fără viață**.

### 🔹 **Masa de joc**
- Reprezentată ca o **matrice 4×5** (4 rânduri, 5 coloane).
- **Jucătorul 1** → are cărțile pe rândurile **2 și 3**.
- **Jucătorul 2** → are cărțile pe rândurile **0 și 1**.
- **Cărțile sunt shiftate la stânga** dacă una este eliminată dintr-un rând.

### 🔹 **Tipuri de cărți**
- **Minioni** → Cărți de atac și apărare, plasate pe masă.
- **Eroi** → Personajele principale ale jucătorilor, având abilități unice.
- **Abilități speciale** → Fiecare carte specială poate folosi o abilitate unică.

---

## 🃏 **Tipuri de cărți**
### 🔹 **Minioni**
Fiecare minion are următoarele atribute:
- **Mana** → Costul necesar pentru a plasa cartea pe masă.
- **Atac** → Punctele de atac folosite pentru a lovi inamicul.
- **Viață** → Când ajunge la `0`, cartea este eliminată.
- **Descriere, culoare și nume** → Atribute vizuale și tematice.

#### 🔹 **Minioni cu abilități speciale**
- **The Ripper** → Scade atacul unui minion inamic cu `2` puncte.
- **Miraj** → Face schimb între viața lui și a unui minion advers.
- **The Cursed One** → Face schimb între atac și viață.
- **Disciple** → Oferă +2 viață unui minion aliat.

#### 🔹 **Reguli pentru minioni**
- **Plasarea pe masă** → Se face conform regulilor fiecărui tip.
- **Atac și utilizare abilități** → O carte **nu poate ataca și folosi o abilitate în aceeași tură**.
- **Minionii "frozen"** → Nu pot ataca până la finalul turei curente.
- **Minionii de tip "Tank" (Goliath, Warden)** → Trebuie **atacați primii** de către adversar.

### 🔹 **Eroii**
- **Lord Royce** → Îngheață toate cărțile de pe un rând.
- **Empress Thorina** → Distruge cartea cu cea mai mare viață de pe un rând.
- **King Mudface** → +1 viață pentru toate cărțile de pe un rând.
- **General Kocioraw** → +1 atac pentru toate cărțile de pe un rând.

---

## 🕹️ **Mecanici de joc**
### 🔹 **Tura unui jucător**
- Poate **plasa cărți**, **ataca inamicul**, **folosi abilități speciale**.
- Cărțile **nu pot ataca de două ori per tură**.
- La finalul turei, cărțile **înghețate sunt deblocate**.

### 🔹 **Atacuri și interacțiuni**
- **Atac între minioni** → Se aplică **damage** și se verifică dacă cartea inamică este eliminată.
- **Atac asupra eroului advers** → Scade viața eroului.
- **Utilizarea abilităților** → Abilitățile eroilor și minionilor modifică starea jocului.

### 🔹 **Gestionarea manei**
- În **fiecare rundă**, jucătorii primesc **mai multă mana**, până la un **maxim de 10**.
- Mana este folosită pentru a **plasa cărți** și **activa abilități**.

---

## 📌 **Implementare tehnică**
### 🔹 **Structura proiectului**
- **cards/** → Definiția cărților (Minioni, Eroi).
- **game/** → Logica de joc (gestionare runde, atacuri, abilități).
- **player/** → Gestionarea jucătorilor (mana, cărți, statistici).
- **utils/** → Funcții auxiliare pentru procesarea comenzilor.

### 🔹 **Clase principale**
- **`Card`** → Clasa de bază pentru toate cărțile.
- **`Minion`** → Extinde `Card`, conține atribute specifice minionilor.
- **`Hero`** → Extinde `Card`, implementează abilități unice.
- **`Player`** → Stochează informații despre un jucător.
- **`Game`** → Gestionarea rundelor, atacurilor și regulilor de joc.
- **`CommandProcessor`** → Procesează comenzile AI-ului.

---

## 🏁 **Exemplu de joc**
```bash
> startGame
Game started. Player One vs. Player Two.

> placeCard 0 3
Player One placed a card on row 3.

> attackCard 2 1
Minion on row 2 attacks minion on row 1.

> useHeroAbility 1
Player One uses hero ability on row 1.

> endTurn
Player One ends their turn.

> getGameStatus
Player One: 15 HP | Player Two: 20 HP
```


---

## 🎯 **Concluzie**
Acest proiect implementează un framework de joc de cărți turn-based, punând accent pe principiile POO și gestionarea interacțiunilor dintre obiecte. Jocul respectă mecanici clasice de card games și oferă un sistem modular pentru extindere ulterioară.

🚀 **Proiect realizat cu pasiune pentru Programarea Orientată pe Obiecte!** 🚀