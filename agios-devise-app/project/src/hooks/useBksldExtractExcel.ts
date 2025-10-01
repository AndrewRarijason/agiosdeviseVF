import { exportBksldExcel } from "../services/extractionBksldService.ts";
import { useBksldExtract } from "./useBksldExtract.ts";
import { importBksldService } from "../services/importBksldService.ts";
import { useState } from "react";

export const useBksldExtractExcel = (date: string) => {
    const [progress, setProgress] = useState(0);

    // Utilise le hook personnalisé pour gérer l'export
    const { isExporting, handleExport } = useBksldExtract(
        date,
        async (date) => {
            setProgress(0);
            await importBksldService(date, setProgress);
            setProgress(100);
            const response = await exportBksldExcel(date);
            return response.data as Blob;
        },
        "xlsx"
    );

    return { isExporting, handleExport, progress };
};