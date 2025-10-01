import React from 'react';
import { Clock, FileText, CheckCircle, AlertCircle, Download, Upload, Database } from 'lucide-react';

interface VerificationResult {
    date_heure_debut: string;
    date_heure_fin: string;
    nbtotal_comptes_traites: number;
    nombre_compte_KO: number;
    ratio_KO: string;
    path_dossier: string;
}

interface ExportResult {
    nbtotal_comptes_traites: number;
    path_dossier: string;
}

interface Dates {
    premierJour: string;
    dernierJour: string;
    premierJourSuivant: string;
}

interface VerificationResultsProps {
    verificationResult: VerificationResult | null;
    formatDateTime: (date: string) => string;
    selectedExcelFile: File | null;
    setSelectedExcelFile: (file: File | null) => void;
    exportPdfFromExcel: (file: File, dateDebut: string, dateFin: string) => void;
    exportPdf: (dateDebut: string, dateFin: string, dateDernierJouvre?: string) => void;
    dates: Dates;
    loadingExcel: boolean;
    loadingAll: boolean;
    errorExcel: string | null;
    resultExcel: ExportResult | null;
    errorAll: string | null;
    resultAll: ExportResult | null;
    selectedEchelleExcelFile: File | null;
    setSelectedEchelleExcelFile: (file: File | null) => void;
    exportEchellePdfFromExcel: (file: File, dateDebut: string, dateFin: string, dateDernierJouvre: string) => void;
    exportEchellePdf: (dateDebut: string, dateFin: string, dateDernierJouvre: string) => void;
    loadingEchelleExcel: boolean;
    loadingEchelleAll: boolean;
    errorEchelleExcel: string | null;
    resultEchelleExcel: ExportResult | null;
    errorEchelleAll: string | null;
    resultEchelleAll: ExportResult | null;
}

const VerificationResults: React.FC<VerificationResultsProps> = ({
                                                                     verificationResult,
                                                                     formatDateTime,
                                                                     selectedExcelFile,
                                                                     setSelectedExcelFile,
                                                                     exportPdfFromExcel,
                                                                     exportPdf,
                                                                     dates,
                                                                     loadingExcel,
                                                                     loadingAll,
                                                                     errorExcel,
                                                                     resultExcel,
                                                                     errorAll,
                                                                     resultAll,
                                                                     errorEchelleExcel,
                                                                     resultEchelleExcel,
                                                                     errorEchelleAll,
                                                                     resultEchelleAll
                                                                 }) => {
    if (!verificationResult) {
        return null;
    }

    const getStatusColor = (count: number) => {
        if (count === 0) return 'text-green-600 bg-green-100';
        if (count < 10) return 'text-yellow-600 bg-yellow-100';
        return 'text-red-600 bg-red-100';
    };

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-8 border-b border-gray-200">
                <div className="flex items-center space-x-3 mb-8">
                    <div className="p-3 bg-gradient-to-br from-green-500 to-green-600 rounded-xl">
                        <CheckCircle className="w-6 h-6 text-white" />
                    </div>
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900">Résultats de la Vérification</h2>
                        <p className="text-gray-600 mt-1">Analyse complète des soldes de comptes</p>
                    </div>
                </div>

                {/* Results Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                    <div className="bg-gradient-to-br from-blue-50 to-blue-100 rounded-xl p-6 border border-blue-200">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-blue-700 mb-1">Date/Heure Début</p>
                                <p className="text-lg font-bold text-blue-900">
                                    {formatDateTime(verificationResult.date_heure_debut)}
                                </p>
                            </div>
                            <div className="p-2 bg-blue-200 rounded-lg">
                                <Clock className="w-5 h-5 text-blue-600" />
                            </div>
                        </div>
                    </div>

                    <div className="bg-gradient-to-br from-purple-50 to-purple-100 rounded-xl p-6 border border-purple-200">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-purple-700 mb-1">Date/Heure Fin</p>
                                <p className="text-lg font-bold text-purple-900">
                                    {formatDateTime(verificationResult.date_heure_fin)}
                                </p>
                            </div>
                            <div className="p-2 bg-purple-200 rounded-lg">
                                <Clock className="w-5 h-5 text-purple-600" />
                            </div>
                        </div>
                    </div>

                    <div className="bg-gradient-to-br from-green-50 to-green-100 rounded-xl p-6 border border-green-200">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-green-700 mb-1">Comptes Traités</p>
                                <p className="text-2xl font-bold text-green-900">
                                    {verificationResult.nbtotal_comptes_traites.toLocaleString()}
                                </p>
                            </div>
                            <div className="p-2 bg-green-200 rounded-lg">
                                <Database className="w-5 h-5 text-green-600" />
                            </div>
                        </div>
                    </div>

                    <div className={`rounded-xl p-6 border ${
                        verificationResult.nombre_compte_KO === 0
                            ? 'bg-gradient-to-br from-green-50 to-green-100 border-green-200'
                            : 'bg-gradient-to-br from-red-50 to-red-100 border-red-200'
                    }`}>
                        <div className="flex items-center justify-between">
                            <div>
                                <p className={`text-sm font-medium mb-1 ${
                                    verificationResult.nombre_compte_KO === 0 ? 'text-green-700' : 'text-red-700'
                                }`}>
                                    Comptes KO
                                </p>
                                <p className={`text-2xl font-bold ${
                                    verificationResult.nombre_compte_KO === 0 ? 'text-green-900' : 'text-red-900'
                                }`}>
                                    {verificationResult.nombre_compte_KO}
                                </p>
                            </div>
                            <div className={`p-2 rounded-lg ${
                                verificationResult.nombre_compte_KO === 0 ? 'bg-green-200' : 'bg-red-200'
                            }`}>
                                {verificationResult.nombre_compte_KO === 0 ? (
                                    <CheckCircle className="w-5 h-5 text-green-600" />
                                ) : (
                                    <AlertCircle className="w-5 h-5 text-red-600" />
                                )}
                            </div>
                        </div>
                    </div>

                    <div className="bg-gradient-to-br from-yellow-50 to-yellow-100 rounded-xl p-6 border border-yellow-200">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-yellow-700 mb-1">Ratio KO</p>
                                <p className="text-2xl font-bold text-yellow-900">
                                    {verificationResult.ratio_KO}
                                </p>
                            </div>
                            <div className="p-2 bg-yellow-200 rounded-lg">
                                <FileText className="w-5 h-5 text-yellow-600" />
                            </div>
                        </div>
                    </div>

                    <div className="bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-6 border border-gray-200 md:col-span-2 lg:col-span-1">
                        <div className="flex items-start justify-between">
                            <div className="flex-1">
                                <p className="text-sm font-medium text-gray-700 mb-2">Dossier de Sortie</p>
                                <p className="text-sm text-gray-900 font-mono bg-white p-2 rounded border break-all">
                                    {verificationResult.path_dossier}
                                </p>
                            </div>
                            <div className="p-2 bg-gray-200 rounded-lg ml-3">
                                <FileText className="w-5 h-5 text-gray-600" />
                            </div>
                        </div>
                    </div>
                </div>

                {/* Export PDF Section */}
                <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-xl p-6 border border-gray-200">
                    <div className="flex items-center space-x-3 mb-6">
                        <div className="p-2 bg-white rounded-lg shadow-sm">
                            <Download className="w-5 h-5 text-[#581D74]" />
                        </div>
                        <div>
                            <h3 className="text-lg font-semibold text-gray-900">Export des Avis PDF</h3>
                            <p className="text-sm text-gray-600">Générer les documents PDF depuis Excel ou pour tous les comptes</p>
                        </div>
                    </div>

                    <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
                        <div className="flex items-center gap-4 flex-1">
                            <div className="relative flex-1 max-w-md">
                                <input
                                    type="text"
                                    readOnly
                                    value={selectedExcelFile ? selectedExcelFile.name : "Aucun fichier sélectionné"}
                                    onClick={() => document.getElementById('excel-file')?.click()}
                                    className="w-full px-4 py-3 border border-gray-300 rounded-xl bg-white cursor-pointer focus:outline-none focus:ring-2 focus:ring-[#581D74] hover:border-gray-400 transition-all duration-200 text-gray-600 font-medium pr-12"
                                    placeholder="Sélectionner un fichier Excel..."
                                />
                                <Upload className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 pointer-events-none" />
                            </div>
                            <input
                                id="excel-file"
                                type="file"
                                accept=".xlsx,.xls"
                                onChange={e => {
                                    if (e.target.files && e.target.files[0]) {
                                        setSelectedExcelFile(e.target.files[0]);
                                    }
                                }}
                                className="hidden"
                            />
                        </div>

                        <div className="flex gap-3">
                            <button
                                onClick={() => {
                                    if (selectedExcelFile) {
                                        exportPdfFromExcel(selectedExcelFile, dates.premierJour, dates.dernierJour);
                                    }
                                }}
                                disabled={loadingExcel || !selectedExcelFile}
                                className={`flex items-center px-6 py-3 bg-white border border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all duration-200 font-medium shadow-sm hover:shadow-md ${
                                    loadingExcel || !selectedExcelFile ? 'opacity-50 cursor-not-allowed' : ''
                                }`}
                            >
                                {loadingExcel ? (
                                    <>
                                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-gray-600 mr-2"></div>
                                        Export...
                                    </>
                                ) : (
                                    <>
                                        <FileText className="w-4 h-4 mr-2" />
                                        Depuis Excel
                                    </>
                                )}
                            </button>
                            <button
                                onClick={() => exportPdf(dates.premierJour, dates.dernierJour, dates.premierJourSuivant)}
                                disabled={loadingAll}
                                className={`flex items-center px-6 py-3 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                    loadingAll ? 'opacity-50 cursor-not-allowed' : ''
                                }`}
                            >
                                {loadingAll ? (
                                    <>
                                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                        Export...
                                    </>
                                ) : (
                                    <>
                                        <Download className="w-4 h-4 mr-2" />
                                        Tous
                                    </>
                                )}
                            </button>
                        </div>
                    </div>
                </div>

                {/* Result Messages */}
                <div className="mt-6 space-y-3">
                    {errorExcel && (
                        <div className="p-4 bg-red-50 border border-red-200 rounded-xl">
                            <div className="flex items-center">
                                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                <span className="text-red-800 font-medium">{errorExcel}</span>
                            </div>
                        </div>
                    )}
                    {resultExcel && (
                        <div className="p-4 bg-green-50 border border-green-200 rounded-xl">
                            <div className="flex items-center">
                                <CheckCircle className="w-5 h-5 text-green-600 mr-2" />
                                <div className="text-green-800 font-medium">
                                    <p>{resultExcel.nbtotal_comptes_traites} avis générés avec succès</p>
                                    <p className="text-sm text-green-700 mt-1 font-mono">Dossier: {resultExcel.path_dossier}</p>
                                </div>
                            </div>
                        </div>
                    )}
                    {errorAll && (
                        <div className="p-4 bg-red-50 border border-red-200 rounded-xl">
                            <div className="flex items-center">
                                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                <span className="text-red-800 font-medium">{errorAll}</span>
                            </div>
                        </div>
                    )}
                    {resultAll && (
                        <div className="p-4 bg-green-50 border border-green-200 rounded-xl">
                            <div className="flex items-center">
                                <CheckCircle className="w-5 h-5 text-green-600 mr-2" />
                                <div className="text-green-800 font-medium">
                                    <p>{resultAll.nbtotal_comptes_traites} avis générés avec succès</p>
                                    <p className="text-sm text-green-700 mt-1 font-mono">Dossier: {resultAll.path_dossier}</p>
                                </div>
                            </div>
                        </div>
                    )}
                    {errorEchelleExcel && (
                        <div className="p-4 bg-red-50 border border-red-200 rounded-xl">
                            <div className="flex items-center">
                                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                <span className="text-red-800 font-medium">{errorEchelleExcel}</span>
                            </div>
                        </div>
                    )}
                    {resultEchelleExcel && (
                        <div className="p-4 bg-green-50 border border-green-200 rounded-xl">
                            <div className="flex items-center">
                                <CheckCircle className="w-5 h-5 text-green-600 mr-2" />
                                <div className="text-green-800 font-medium">
                                    <p>{resultEchelleExcel.nbtotal_comptes_traites} avis générés avec succès</p>
                                    <p className="text-sm text-green-700 mt-1 font-mono">Dossier: {resultEchelleExcel.path_dossier}</p>
                                </div>
                            </div>
                        </div>
                    )}
                    {errorEchelleAll && (
                        <div className="p-4 bg-red-50 border border-red-200 rounded-xl">
                            <div className="flex items-center">
                                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                <span className="text-red-800 font-medium">{errorEchelleAll}</span>
                            </div>
                        </div>
                    )}
                    {resultEchelleAll && (
                        <div className="p-4 bg-green-50 border border-green-200 rounded-xl">
                            <div className="flex items-center">
                                <CheckCircle className="w-5 h-5 text-green-600 mr-2" />
                                <div className="text-green-800 font-medium">
                                    <p>{resultEchelleAll.nbtotal_comptes_traites} avis générés avec succès</p>
                                    <p className="text-sm text-green-700 mt-1 font-mono">Dossier: {resultEchelleAll.path_dossier}</p>
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default VerificationResults;