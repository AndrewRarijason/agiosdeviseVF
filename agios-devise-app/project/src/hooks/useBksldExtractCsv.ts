import { exportBksldCsv } from "../services/extractionBksldService.ts";
import { useBksldExtract } from "./useBksldExtract.ts";

export const useBksldExtractCsv = (date: string) =>
    useBksldExtract(
        date,
        async (date) => {
            const response = await exportBksldCsv(date);
            return response.data as Blob;
        },
        "csv"
    );