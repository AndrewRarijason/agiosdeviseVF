import { useState, useCallback } from "react";
import { getSumAgiosParTcli } from "../services/agiosTcliService";

export function useAgiosTcli() {
    const [data, setData] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const fetchAgiosTcli = useCallback(async (dateDebut: string, dateFin: string) => {
        setLoading(true);
        setError(null);
        try {
            const result = await getSumAgiosParTcli(dateDebut, dateFin);
            setData(result);
        } catch (e: any) {
            setError(e.message || "Erreur chargement agios Tcli");
        } finally {
            setLoading(false);
        }
    }, []);

    return { data, loading, error, fetchAgiosTcli };
}