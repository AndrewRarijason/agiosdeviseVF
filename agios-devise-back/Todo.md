Todo list :
- Affichage :
  - 
        - Page d'accueil
        - Saisie identifiant et mdp
        - Page d'import (bksld, bkdar,bkhis) avec fenêtre d'information
        - Choix opérateur : Fenêtre d'import pour nouvelles valeurs des taux créditeurs négatifs
        - Choix opérateur : Fenêtre d'import fichier pour les comptes avec dérogation sur tx créditeurs neg
        - Fênetre d'information de statut pour chaque étape et liste comptes absents dans BKDAR avec dérogation

- Métier :
  - 
        - Récupération identifiants
        - Connexion oracle database
        - Méthode insert() des fichiers importés
        - Calcul des agios
        - Choix opérateur : Méthode update() pour taux créditeurs négatifs
        - Génération fichier d'injection
        - Génération éditions (similaires à celles produites pas Amplitude)

- Intégration :
  -
        - Authentication
        - API pour insertion
        - Choix opérateur : API modif nouvelles valeurs taux créditeurs négatifs
        - API d'insertion BKHIS