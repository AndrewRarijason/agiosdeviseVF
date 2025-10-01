import { useState } from 'react';
import { getDashboardArrete } from '../services/dashboardArreteService';

export const useDashboardArrete = () => {
    const [data, setData] = useState<any>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchDashboard = async (dateDebutArrete: string, dateFinArrete: string) => {
        setLoading(true);
        setError(null);
        try {
            const result = await getDashboardArrete(dateDebutArrete, dateFinArrete);
            setData(result);
        } catch (e: any) {
            setError(e.message || 'Erreur lors du chargement du dashboard');
        } finally {
            setLoading(false);
        }
    };

    return { data, loading, error, fetchDashboard };
};