import axios from 'axios';
import { Trimestre } from '../types';

// Fonctions utilitaires pour les dates de trimestre
const getDateDebutTrimestre = (trimestre: Trimestre, annee: number): string => {
    const trimestres: Record<Trimestre, string> = {
        T1: `01/01/${annee}`,
        T2: `01/04/${annee}`,
        T3: `01/07/${annee}`,
        T4: `01/10/${annee}`,
    };
    return trimestres[trimestre];
};

const getDateFinTrimestre = (trimestre: Trimestre, annee: number): string => {
    const trimestres: Record<Trimestre, string> = {
        T1: `31/03/${annee}`,
        T2: `30/06/${annee}`,
        T3: `30/09/${annee}`,
        T4: `31/12/${annee}`,
    };
    return trimestres[trimestre];
};

export const fetchHistoriqueArretes = async (trimestre: Trimestre, annee: number) => {
    const res = await axios.get('/api/arretes/historique', {
        params: {
            trimestre,
            annee,
            dateDebut: getDateDebutTrimestre(trimestre, annee),
            dateFin: getDateFinTrimestre(trimestre, annee),
        },
    });
    return res.data;
};

export const fetchHistoriqueArreteDetail = async (ncp: string, trimestre: Trimestre, annee: string | number) => {
    const res = await axios.get('/api/arretes/historique/detail', {
        params: { ncp, trimestre, annee }
    });
    return res.data;
};
