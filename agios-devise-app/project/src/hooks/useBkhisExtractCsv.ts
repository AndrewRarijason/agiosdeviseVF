import { exportBkhisCsv} from "../services/extractionBkhisService.ts";
import { useBkhisExtract } from "./useBkhisExtract.ts";

export const useBkhisExtractCsv = (dateRef: string, dateFin: string) =>
    useBkhisExtract(
        dateRef, dateFin,
        async (dateRef, dateFin) => {
            const response = await exportBkhisCsv(dateRef, dateFin);
            return response.data as Blob;
        },
        "csv"
    );