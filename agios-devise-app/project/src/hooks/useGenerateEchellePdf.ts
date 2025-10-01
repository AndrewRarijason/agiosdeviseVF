import { useState } from 'react';
import { exportAllEchellePdf, exportEchellePdfFromExcel } from '../services/generateEchellePdfService';

export function useGenerateEchellePdf() {
    const [loadingAll, setLoadingAll] = useState(false);
    const [loadingExcel, setLoadingExcel] = useState(false);
    const [errorAll, setErrorAll] = useState<string | null>(null);
    const [errorExcel, setErrorExcel] = useState<string | null>(null);
    const [resultAll, setResultAll] = useState<any>(null);
    const [resultExcel, setResultExcel] = useState<any>(null);

    const exportPdf = async (dateDebutArrete: string, dateFinArrete: string, dateDernierJouvre: string) => {
        setLoadingAll(true);
        setErrorAll(null);
        setResultAll(null);
        try {
            const res = await exportAllEchellePdf(dateDebutArrete, dateFinArrete, dateDernierJouvre);
            setResultAll(res);
        } catch (e: any) {
            setErrorAll(e?.response?.data?.message || "Erreur lors de l'export PDF");
        } finally {
            setLoadingAll(false);
        }
    };

    const exportPdfFromExcel = async (file: File, dateDebutArrete: string, dateFinArrete: string, dateDernierJouvre: string) => {
        setLoadingExcel(true);
        setErrorExcel(null);
        setResultExcel(null);
        try {
            const res = await exportEchellePdfFromExcel(file, dateDebutArrete, dateFinArrete, dateDernierJouvre);
            setResultExcel(res);
        } catch (e: any) {
            setErrorExcel(e?.response?.data?.message || "Erreur lors de l'export PDF depuis Excel");
        } finally {
            setLoadingExcel(false);
        }
    };

    return {
        loadingAll, loadingExcel,
        errorAll, errorExcel,
        resultAll, resultExcel,
        exportPdf, exportPdfFromExcel,
    };
}