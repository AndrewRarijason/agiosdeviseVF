import { useState } from 'react';
import {fetchHistoriqueArreteDetail, fetchHistoriqueArretes} from '../services/historiqueArretesService';
import { Trimestre } from '../types';

type HistoriqueArrete = {
    ncp: string;
    nomrest: string;
    dev: string;
    soldeFinalCalcule: number;
    netAgios: number;
};

type HistoriqueArreteDetail = {
    age: string;
    dev: string;
    ncp: string;
    nomrest: string;
    soldeFinalCalcule: number;
    dateDebutArrete: string;
    dateFinArrete: string;
    sumMvtCrediteur: number;
    tauxCrediteur: number;
    interetCrediteur: number;
    sumMvtDebiteur: number;
    tauxDebiteur: number;
    interetDebiteur: number;
    netAgios: number;
};

export const useHistoriqueArretes = () => {
    const [data, setData] = useState<HistoriqueArrete[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const getHistorique = async (trimestre: Trimestre, annee: number) => {
        setLoading(true);
        setError(null);
        try {
            const res = await fetchHistoriqueArretes(trimestre, annee);
            setData(res);
        } catch (e: unknown) {
            if (
                typeof e === 'object' &&
                e !== null &&
                'response' in e &&
                typeof (e as { response?: unknown }).response === 'object' &&
                (e as { response?: { data?: { message?: string } } }).response?.data?.message
            ) {
                setError(
                    (e as { response: { data: { message: string } } }).response.data.message
                );
            } else if (e instanceof Error) {
                setError(e.message);
            } else {
                setError('Erreur lors de la récupération');
            }
        } finally {
            setLoading(false);
        }
    };

    return { data, loading, error, getHistorique };
};

export const useHistoriqueArreteDetail = () => {
    const [data, setData] = useState<HistoriqueArreteDetail | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const getDetail = async (ncp: string, trimestre: string, annee: string | number) => {
        setLoading(true);
        setError(null);
        try {
            const res = await fetchHistoriqueArreteDetail(ncp, trimestre, annee);
            setData(res);
        } catch (e: any) {
            setError(e?.message || 'Erreur lors du chargement du détail');
        } finally {
            setLoading(false);
        }
    };

    return { data, loading, error, getDetail };
};