import { exportBkdarCsv } from '../services/extractionBkdarService';
import { useBkdarExtract } from './useBkdarExtract.ts';

export const useBkdarExtractCsv = (date: string) =>
    useBkdarExtract(
        date,
        async (mois, annee) => {
            const response = await exportBkdarCsv(mois, annee);
            return response.data as Blob;
        },
        'csv'
    );