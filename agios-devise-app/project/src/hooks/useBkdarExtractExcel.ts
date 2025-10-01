import { exportBkdarExcel } from '../services/extractionBkdarService';
import { useBkdarExtract } from './useBkdarExtract.ts';
import { importBkdarService } from '../services/importBkdarService.ts';

export const useBkdarExtractExcel = (date: string) =>
    useBkdarExtract(
        date,
        async (mois, annee) => {
            await importBkdarService(mois, annee);
            const response = await exportBkdarExcel(mois, annee);
            return response.data as Blob;
        },
        'xlsx'
    );