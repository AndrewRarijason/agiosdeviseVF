import { useState } from 'react';
import { exportBkdarCsv } from '../services/extractionBkdarService';

export const useBkdarImport = (date: string) => {
    const [isExportingBkdarCsv, setIsImporting] = useState(false);

    const handleImportBkdar = async () => {
        if (!date) {
            alert('Veuillez sélectionner une date.');
            return;
        }
        const d = new Date(date);
        const mois = d.getMonth() + 1;
        const annee = d.getFullYear();
        if (isNaN(mois) || isNaN(annee)) {
            alert('Date invalide.');
            return;
        }
        setIsImporting(true);
        try {
            const blob = await exportBkdarCsv(mois, annee);
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            const now = new Date();
            const pad = (n: number) => n.toString().padStart(2, '0');
            const moisStr = `${pad(mois)}`; // mois sur 2 chiffres
            const dateStr = `${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}`; // yyyymmdd
            const heureStr = `${pad(now.getHours())}${pad(now.getMinutes())}${pad(now.getSeconds())}`; // hhmmss
            a.download = `bkdar_${annee}-${moisStr}_${dateStr}_${heureStr}.csv`;
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Erreur lors de l\'import BKDAR :', error);
        } finally {
            setIsImporting(false);
        }
    };

    return { isExportingBkdarCsv, handleImportBkdar };
};