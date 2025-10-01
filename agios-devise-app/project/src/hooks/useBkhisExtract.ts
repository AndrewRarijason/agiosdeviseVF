import { useState } from "react";

export const useBkhisExtract = (
    dateRef: string,
    dateFin: string,
    exportFunc: (dateRef: string, dateFin: string) => Promise<Blob>,
    extension: string
) => {
    const [isExporting, setIsExporting] = useState(false);
    const handleExport = async () => {
        if (!dateRef || !dateFin) {
            alert("Veuillez sélectionner une période valide.");
            return;
        }
        setIsExporting(true);
        try {
            const blob = await exportFunc(dateRef, dateFin);
            const datenow = new Date();
            const pad = (n: number) => n.toString().padStart(2, "0");
            const dateStr = `${datenow.getFullYear()}${pad(datenow.getMonth() + 1)}${pad(datenow.getDate())}`;
            const heureStr = `${pad(datenow.getHours())}${pad(datenow.getMinutes())}${pad(datenow.getSeconds())}`;
            const a = document.createElement("a");
            a.href = window.URL.createObjectURL(blob);
            a.download = `bkhis_${dateRef}_to_${dateFin}_${dateStr}_${heureStr}.${extension}`;
            a.click();
            a.remove();
            window.URL.revokeObjectURL(a.href);
        } catch (error) {
            console.error("Erreur lors de l'export BKHIS :", error);
        } finally {
            setIsExporting(false);
        }
    };

    return { isExporting, handleExport };
};