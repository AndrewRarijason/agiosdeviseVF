import { useState } from 'react';
import { exportInjection } from '../services/injectionService';

export const useInjectionExport = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [result, setResult] = useState<any>(null);

    const handleExport = async (
        idBmoi: string,
        dtDebutArrete: string,
        dtFinArrete: string,
        dtDernierJourOuvre: string,
        dtJourOuvreNextTrim: string
    ) => {
        setLoading(true);
        setError(null);
        try {
            const data = await exportInjection(idBmoi, dtDebutArrete, dtFinArrete, dtDernierJourOuvre, dtJourOuvreNextTrim);
            setResult(data);
        } catch (e: any) {
            setError(e.message || 'Erreur lors de l\'export');
        } finally {
            setLoading(false);
        }
    };

    return { loading, error, result, handleExport };
};