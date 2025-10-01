// useArreteCompteVerification.ts
import { useState, useCallback } from 'react';
import { verifierSoldesArreteCompte } from '../services/arreteCompteService';

export function useArreteCompteVerification() {
    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState<any>(null);
    const [error, setError] = useState<string | null>(null);
    const [progress, setProgress] = useState({
        current: 0,
        total: 0,
        percentage: 0,
        status: 'idle' // 'idle' | 'processing' | 'completed' | 'error'
    });

    const verifier = useCallback(async (dateDebutArrete: string, dateFinArrete: string, dateDernierJouvre: string) => {
        setLoading(true);
        setError(null);
        setProgress({
            current: 0,
            total: 0,
            percentage: 0,
            status: 'processing'
        });

        try {
            const eventSource = new EventSource('/api/arrete-compte/transactions/excel/progress');

            eventSource.onmessage = (event) => {
                const progressData = JSON.parse(event.data);
                setProgress({
                    current: progressData.processed,
                    total: progressData.total,
                    percentage: progressData.percentage,
                    status: progressData.status.toLowerCase()
                });
            };

            eventSource.onerror = (error) => {
                console.error('SSE Error:', error);
                eventSource.close();
            };

            const data = await verifierSoldesArreteCompte(dateDebutArrete, dateFinArrete, dateDernierJouvre);

            setResult(data);
            setProgress(prev => ({ ...prev, status: 'completed' }));
            eventSource.close();

        } catch (e: any) {
            setError('Erreur lors de la vérification');
            setProgress(prev => ({ ...prev, status: 'error' }));
        } finally {
            setLoading(false);
        }
    }, []);

    return { loading, result, error, progress, verifier };
}