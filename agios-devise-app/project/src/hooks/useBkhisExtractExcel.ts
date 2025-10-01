import { exportBkhisExcel } from "../services/extractionBkhisService.ts";
import { useBkhisExtract } from "./useBkhisExtract.ts";
import { importBkhisService } from "../services/importBkhisService.ts";

export const useBkhisExtractExcel = (dateRef: string, dateFin: string) =>
    useBkhisExtract(
        dateRef, dateFin,
        async (dateRef, dateFin) => {
            await importBkhisService(dateRef, dateFin);
            const response = await exportBkhisExcel(dateRef, dateFin);
            return response.data as Blob;
        },
        "xlsx"
    );