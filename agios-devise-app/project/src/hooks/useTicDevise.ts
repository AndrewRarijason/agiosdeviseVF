import React, { useState, useRef } from 'react';
import { importTicDevise, fetchTicDevises } from '../services/ticService';

type ImportStatus = 'success' | 'error';

export function useTicDevise(
    onImportResult?: (status: ImportStatus, message: string) => void
) {
    const [loading, setLoading] = useState(false);
    const [ticData, setTicData] = useState<unknown[]>([]);
    const fileInputRef = useRef<HTMLInputElement>(null);

    // Récupère les taux depuis l'API
    const handleVisualiser = async () => {
        setLoading(true);
        try {
            const data = await fetchTicDevises();
            setTicData(data);
        } catch {
            //gérer l'erreur
        }
        setLoading(false);
    };

    // Importation de fichier
    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (!e.target.files?.length) return;
        setLoading(true);
        try {
            await importTicDevise(e.target.files[0]);
            setLoading(false);
            await handleVisualiser();
            onImportResult?.('success', 'Importation réussie');
        } catch {
            setLoading(false);
            onImportResult?.('error', 'Importation échouée');
        }
    };

    const handleImportClick = () => {
        fileInputRef.current?.click();
    };

    return {
        loading,
        ticData,
        fileInputRef,
        handleVisualiser,
        handleImportClick,
        handleFileChange
    };
}