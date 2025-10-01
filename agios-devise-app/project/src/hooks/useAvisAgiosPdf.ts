import { useState } from 'react';
import { exportAllAvisAgiosPdf, exportAvisAgiosFromExcel } from '../services/avisAgiosPdfService';

export function useAvisAgiosPdf() {
    const [loadingAll, setLoadingAll] = useState(false);
    const [errorAll, setErrorAll] = useState<string | null>(null);
    const [resultAll, setResultAll] = useState<{ nbtotal_comptes_traites: number, path_dossier: string } | null>(null);

    const [loadingExcel, setLoadingExcel] = useState(false);
    const [errorExcel, setErrorExcel] = useState<string | null>(null);
    const [resultExcel, setResultExcel] = useState<{ nbtotal_comptes_traites: number, path_dossier: string } | null>(null);

    const exportPdf = async (dateDebut: string, dateFin: string) => {
        setLoadingAll(true);
        setErrorAll(null);
        setResultAll(null);
        try {
            const res = await exportAllAvisAgiosPdf(dateDebut, dateFin);
            setResultAll(res);
        } catch (e: any) {
            setErrorAll('Erreur lors de l\'export PDF');
        } finally {
            setLoadingAll(false);
        }
    };

    const exportPdfFromExcel = async (file: File, dateDebut: string, dateFin: string) => {
        setLoadingExcel(true);
        setErrorExcel(null);
        setResultExcel(null);
        try {
            const res = await exportAvisAgiosFromExcel(file, dateDebut, dateFin);
            setResultExcel(res);
        } catch (e: any) {
            const backendMsg = e?.response?.data?.message || e?.response?.data?.error || e?.message;
            setErrorExcel(backendMsg);
        } finally {
            setLoadingExcel(false);
        }
    };

    return {
        exportPdf, exportPdfFromExcel,
        loadingAll, errorAll, resultAll,
        loadingExcel, errorExcel, resultExcel
    };
}