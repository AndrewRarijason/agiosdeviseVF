import { useState } from "react";

export const useBksldExtract = (
    date: string,
    exportFunc: (date: string) => Promise<Blob>,
    extension: string
) => {
    const [isExporting, setIsExporting] = useState(false);

    const handleExport = async () => {
        if (!date) {
            alert("Veuillez sélectionner une date.");
            return;
        }
        setIsExporting(true);
        try {
            const blob = await exportFunc(date); // plus de conversion
            const url = window.URL.createObjectURL(blob);
            const now = new Date();
            const pad = (n: number) => n.toString().padStart(2, "0");
            const dateStr = `${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}`;
            const heureStr = `${pad(now.getHours())}${pad(now.getMinutes())}${pad(now.getSeconds())}`;
            const a = document.createElement("a");
            a.href = url;
            a.download = `bksld_${date}_${dateStr}_${heureStr}.${extension}`;
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Erreur lors de l'export BKSLD :", error);
        } finally {
            setIsExporting(false);
        }
    };

    return { isExporting, handleExport };
};