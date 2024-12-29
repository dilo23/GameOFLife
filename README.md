# 🌱 **Jeu de la Vie (Game of Life)** 🌱

## 🎮 **Description**

Le **Jeu de la Vie** est une simulation créée par **John Conway** en 1970, qui se déroule sur une grille 2D où chaque cellule peut être soit vivante (🟢), soit morte (⚪). Les cellules évoluent selon des règles simples, et cette dynamique produit des motifs de plus en plus complexes. Ce projet implémente ce jeu en Java, avec une interface graphique utilisant **Swing**.

## ⚙️ **Règles du Jeu**

1. **Sous-population** : Une cellule vivante avec moins de 2 voisines vivantes meurt (💀).
2. **Survie** : Une cellule vivante avec 2 ou 3 voisines vivantes reste vivante (✔️).
3. **Sur-population** : Une cellule vivante avec plus de 3 voisines vivantes meurt (💀).
4. **Reproduction** : Une cellule morte avec exactement 3 voisines vivantes devient vivante (🌱).

## 🚀 **Fonctionnalités**

- **Interface Graphique** avec Swing : Affiche la grille et permet d'interagir avec elle (🖱️).
- **Contrôle de la simulation** : Démarrer, mettre en pause et ajuster la vitesse de la simulation (⏸️ / ▶️).
- **Modification interactive de l'état des cellules** : Cliquez sur les cellules pour les activer/désactiver (🔲🔳).
- **Ajout de motifs prédéfinis** : Glider, Block, Blinker, Toad, etc. (🌀).
- **Sauvegarde et chargement d'état** : Sauvegardez l'état de la grille et chargez-le ultérieurement (💾).
- **Mode Virus** : Simule la propagation d'une infection entre les cellules (🦠).

## 🏗️ **Architecture**

Le projet suit l'architecture **MVC** (Modèle-Vue-Contrôleur) :

- **Modèle** : Gère la logique du jeu (calcul des nouveaux états des cellules).
- **Vue** : Interface graphique utilisant **Swing** pour afficher la grille et les interactions.
- **Contrôleur** : Gère les événements (clics, boutons, etc.) et met à jour le modèle.

## 📦 **Installation**

### Prérequis

- **Java 8 ou supérieur** (☕).

### Cloner le projet

```bash
git clone https://github.com/dilo23/GameOFLife.git
```
## ⚙️ **Compiler et Exécuter**

### Compilez le projet :

```bash
javac -d bin src/*.java
```

## ⚙️ **Exécutez le programme**

### Exécutez le programme :

```bash
java -cp bin Main
```

###✨ **Extensions et Améliorations** : 

Contrôle du Temps de Simulation : Permet à l'utilisateur de mettre en pause et de contrôler la vitesse de la simulation (⏱️).
Environnement Interactif : Les utilisateurs peuvent manipuler directement les cellules de la grille (🖱️).
Motifs Pré-définis : Ajouter des motifs classiques comme le Glider ou Block (🔄).
Sauvegarde et Chargement : Sauvegarder et charger l'état de la grille en utilisant des fichiers XML (📂).
Mode Virus : Simulation de la propagation virale entre les cellules (🦠).


-----------------------------------------------------------------------------------------------------------------------------
