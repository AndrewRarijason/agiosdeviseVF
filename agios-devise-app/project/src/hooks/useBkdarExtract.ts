import { useState } from 'react';

export const useBkdarExtract = (
    date: string,
    exportFunc: (mois: number, annee: number) => Promise<Blob>,
    extension: string
) => {
    const [isExporting, setIsExporting] = useState(false);

    const handleExport = async () => {
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
        setIsExporting(true);
        try {
            const blob = await exportFunc(mois, annee);
            const url = window.URL.createObjectURL(blob);
            const now = new Date();
            const pad = (n: number) => n.toString().padStart(2, '0');
            const moisStr = `${pad(mois)}`;
            const dateStr = `${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}`;
            const heureStr = `${pad(now.getHours())}${pad(now.getMinutes())}${pad(now.getSeconds())}`;
            const a = document.createElement('a');
            a.href = url;
            a.download = `bkdar_${annee}-${moisStr}_${dateStr}_${heureStr}.${extension}`;
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Erreur lors de l\'export BKDAR :', error);
        } finally {
            setIsExporting(false);
        }
    };

    return { isExporting, handleExport };
};