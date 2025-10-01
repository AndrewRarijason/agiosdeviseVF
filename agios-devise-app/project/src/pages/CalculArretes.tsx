import { useNavigate } from 'react-router-dom';
import React, {useEffect, useRef, useState} from 'react';
import Sidebar from '../components/Sidebar.tsx';
import {FileText, Calendar, View, Trash, AlertCircle, CheckCircle, Download, Upload, Database, Settings, Clock, ChevronLeft} from 'lucide-react';
import { useBkdarExtractCsv } from '../hooks/useBkdarExtractCsv.ts';
import { useBksldExtractCsv } from '../hooks/useBksldExtractCsv.ts';
import { useBksldExtractExcel } from "../hooks/useBksldExtractExcel.ts";
import {useBkdarExtractExcel} from "../hooks/useBkdarExtractExcel.ts";
import { useBkhisExtractExcel } from '../hooks/useBkhisExtractExcel.ts';
import { useBkhisExtractCsv } from '../hooks/useBkhisExtractCsv.ts';
import { useTicDevise } from '../hooks/useTicDevise';
import { useArreteCompteVerification } from '../hooks/useArreteCompteVerification';
import { formatDateTime, getNextDay } from '../utils/dateUtils';
import { useDerrogationImport } from '../hooks/useDerrogationImport';
import {useAvisAgiosPdf} from "../hooks/useAvisAgiosPdf.ts";
import Header from "../components/Header.tsx";
import { useGenerateEchellePdf } from '../hooks/useGenerateEchellePdf';
import VerificationResults from '../components/VerificationResults';
import FileImportSection from "../components/FileImportSection.tsx";
import {useInjectionExport} from "../hooks/useInjectionExport.ts";
import {deleteDerogation} from "../services/derrogationService.ts";

const CalculArretes: React.FC = () => {
    const [dates, setDates] = useState({
        premierJour: '',
        dernierJour: '',
        premierJourOuvre: '',
        premierJourSuivant: ''
    });
    const [showBMOIPopup, setShowBMOIPopup] = useState(false);
    const [bmoiId, setBmoiId] = useState('');

    const handleExportClick = () => {
        setShowBMOIPopup(true);
    };

    const handleConfirmExport = () => {
        setShowBMOIPopup(false);
        handleExport(
            bmoiId,
            dates.premierJour,
            dates.dernierJour,
            dates.premierJourSuivant,
            dates.premierJourOuvre
        );
    };

    const [openBksld, setOpenBksld] = useState(false);
    const [openBkdar, setOpenBkdar] = useState(false);
    const [openBkhis, setOpenBkhis] = useState(false);
    const dropdownRefBksld = useRef<HTMLDivElement>(null);
    const dropdownRefBkdar = useRef<HTMLDivElement>(null);
    const dropdownRefBkhis = useRef<HTMLDivElement>(null);
    const [selectedDerroFile, setSelectedDerroFile] = useState<File | null>(null);
    const navigate = useNavigate();
    const [deleteMessage, setDeleteMessage] = useState('');
    const [isDeletingDerro, setIsDeletingDerro] = useState(false);

    const { isExporting: isExportingBksldCsv, handleExport: handleExportBksldCsv } = useBksldExtractCsv(dates.dernierJour);
    const { isExporting: isExportingBksldExcel, handleExport: handleExportBksldExcel, progress } = useBksldExtractExcel(dates.dernierJour);
    const { isExporting: isExportingBkdarCsv, handleExport: handleExportBkdarCsv } = useBkdarExtractCsv(dates.dernierJour);
    const { isExporting: isExportingBkdarExcel, handleExport: handleExportBkdarExcel } = useBkdarExtractExcel(dates.dernierJour);
    const { isExporting: isExportingBkhisCsv, handleExport: handleExportBkhisCsv } = useBkhisExtractCsv(dates.premierJour, dates.dernierJour);
    const { isExporting: isExportingBkhisExcel, handleExport: handleExportBkhisExcel } = useBkhisExtractExcel(dates.premierJour, dates.dernierJour);
    const [selectedExcelFile, setSelectedExcelFile] = useState<File | null>(null);
    const { loading: loadingInjection, error: errorInjection, result: resultInjection, handleExport } = useInjectionExport();

    const {
        exportPdf, exportPdfFromExcel,
        loadingAll, errorAll, resultAll,
        loadingExcel, errorExcel, resultExcel
    } = useAvisAgiosPdf();

    const [selectedEchelleExcelFile, setSelectedEchelleExcelFile] = useState<File | null>(null);
    const {
        exportPdf: exportEchellePdf,
        exportPdfFromExcel: exportEchellePdfFromExcel,
        loadingAll: loadingEchelleAll,
        errorAll: errorEchelleAll,
        resultAll: resultEchelleAll,
        loadingExcel: loadingEchelleExcel,
        errorExcel: errorEchelleExcel,
        resultExcel: resultEchelleExcel
    } = useGenerateEchellePdf();

    const {
        loading: ticDeviseLoading,
        fileInputRef,
        handleImportClick,
        handleFileChange
    } = useTicDevise((status, message) => {
        setImportStatus(status);
        setImportMessage(message);
    });

    const {
        loading: isVerifying,
        result: verificationResult,
        error: verificationError,
        progress: verificationProgress,
        verifier
    } = useArreteCompteVerification();

    const [importMessage, setImportMessage] = useState('');
    const [importStatus, setImportStatus] = useState<'success' | 'error' | ''>('');

    const {
        derroMessage,
        derroStatus,
        isImportingDerro,
        handleImportDerrogation,
    } = useDerrogationImport();

    const handleVerification = async () => {
        await verifier(dates.premierJour, dates.dernierJour, dates.premierJourOuvre);
    };

    useEffect(() => {
        const savedDates = localStorage.getItem('arretesDates');
        if (savedDates) setDates(JSON.parse(savedDates));
    }, []);

    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (dropdownRefBksld.current && !dropdownRefBksld.current.contains(event.target as Node)) {
                setOpenBksld(false);
            }
            if (dropdownRefBkdar.current && !dropdownRefBkdar.current.contains(event.target as Node)) {
                setOpenBkdar(false);
            }
            if (dropdownRefBkhis.current && !dropdownRefBkhis.current.contains(event.target as Node)) {
                setOpenBkhis(false);
            }
        }
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleDateChange = (field: string, value: string) => {
        setDates(prev => {
            const updated = { ...prev, [field]: value };
            localStorage.setItem('arretesDates', JSON.stringify(updated));
            return updated;
        });
    };

    const handleBack = () => {
        navigate('/accueil');
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex">
            <Sidebar currentPage="Calcul arrêtés trimestriels" />
            <div className="flex-1 ml-64 transition-all duration-300">
                <Header title="CALCUL ARRÊTÉS TRIMESTRIELS" onBack={handleBack}/>
                <main className="p-8">
                    <div className="max-w-7xl mx-auto space-y-8">

                        {/* Date Input Section */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-8 border-b border-gray-200">
                                <div className="flex items-center space-x-3 mb-8">
                                    <div className="p-3 bg-gradient-to-br from-[#581D74] to-[#6B2C87] rounded-xl">
                                        <Calendar className="w-6 h-6 text-white" />
                                    </div>
                                    <div>
                                        <h2 className="text-2xl font-bold text-gray-900">Configuration des Dates</h2>
                                        <p className="text-gray-600 mt-1">Définissez les périodes pour le calcul des arrêtés</p>
                                    </div>
                                </div>

                                <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">
                                            Premier jour du trimestre passé
                                        </label>
                                        <div className="relative">
                                            <Calendar className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
                                            <input
                                                type="date"
                                                value={dates.premierJour}
                                                onChange={(e) => handleDateChange('premierJour', e.target.value)}
                                                className="w-full pl-12 pr-4 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 bg-white hover:border-gray-400 font-medium"
                                            />
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">
                                            Dernier jour du trimestre passé
                                        </label>
                                        <div className="relative">
                                            <Calendar className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
                                            <input
                                                type="date"
                                                value={dates.dernierJour}
                                                onChange={(e) => handleDateChange('dernierJour', e.target.value)}
                                                className="w-full pl-12 pr-4 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 bg-white hover:border-gray-400 font-medium"
                                            />
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">
                                            Premier jour ouvré du trimestre suivant
                                        </label>
                                        <div className="relative">
                                            <Calendar className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
                                            <input
                                                type="date"
                                                value={dates.premierJourOuvre}
                                                onChange={(e) => handleDateChange('premierJourOuvre', e.target.value)}
                                                className="w-full pl-12 pr-4 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 bg-white hover:border-gray-400 font-medium"
                                            />
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">
                                            Dernier jour ouvré du trimestre précédent
                                        </label>
                                        <div className="relative">
                                            <Calendar className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 z-10" />
                                            <input
                                                type="date"
                                                value={dates.premierJourSuivant}
                                                onChange={(e) => handleDateChange('premierJourSuivant', e.target.value)}
                                                className="w-full pl-12 pr-4 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 bg-white hover:border-gray-400 font-medium"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* File Import Section */}
                        <FileImportSection
                            openBksld={openBksld}
                            setOpenBksld={setOpenBksld}
                            openBkdar={openBkdar}
                            setOpenBkdar={setOpenBkdar}
                            openBkhis={openBkhis}
                            setOpenBkhis={setOpenBkhis}
                            dropdownRefBksld={dropdownRefBksld}
                            dropdownRefBkdar={dropdownRefBkdar}
                            dropdownRefBkhis={dropdownRefBkhis}
                            isExportingBksldExcel={isExportingBksldExcel}
                            isExportingBksldCsv={isExportingBksldCsv}
                            handleExportBksldExcel={handleExportBksldExcel}
                            handleExportBksldCsv={handleExportBksldCsv}
                            progress={progress}
                            isExportingBkdarExcel={isExportingBkdarExcel}
                            isExportingBkdarCsv={isExportingBkdarCsv}
                            handleExportBkdarExcel={handleExportBkdarExcel}
                            handleExportBkdarCsv={handleExportBkdarCsv}
                            isExportingBkhisExcel={isExportingBkhisExcel}
                            isExportingBkhisCsv={isExportingBkhisCsv}
                            handleExportBkhisExcel={handleExportBkhisExcel}
                            handleExportBkhisCsv={handleExportBkhisCsv}
                        />

                        {/* TIC Devise and Derogation Section */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-8 border-b border-gray-200">
                                <div className="flex items-center space-x-3 mb-8">
                                    <div className="p-3 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl">
                                        <Settings className="w-6 h-6 text-white" />
                                    </div>
                                    <div>
                                        <h2 className="text-2xl font-bold text-gray-900">Configuration Avancée</h2>
                                        <p className="text-gray-600 mt-1">Gestion des taux créditeurs et dérogations</p>
                                    </div>
                                </div>

                                <div className="space-y-6">
                                    {/* Import TIC */}
                                    <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-xl p-6 border border-gray-200">
                                        <div className="flex items-center justify-between">
                                            <div className="flex items-center space-x-4">
                                                <div className="p-2 bg-white rounded-lg shadow-sm">
                                                    <View className="w-5 h-5 text-[#581D74]" />
                                                </div>
                                                <div>
                                                    <h3 className="font-semibold text-gray-900">Taux Créditeurs Négatifs</h3>
                                                    <p className="text-sm text-gray-600">Visualiser ou importer les nouveaux taux (facultatif)</p>
                                                </div>
                                            </div>
                                            <div className="flex gap-3">
                                                <button
                                                    onClick={() => navigate('/gestion-ticdevise')}
                                                    className="flex items-center px-6 py-3 bg-white border border-gray-300 text-gray-700 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all duration-200 font-medium shadow-sm hover:shadow-md"
                                                >
                                                    <View className="w-4 h-4 mr-2" />
                                                    Visualiser
                                                </button>
                                                <button
                                                    onClick={handleImportClick}
                                                    disabled={ticDeviseLoading}
                                                    className={`flex items-center px-6 py-3 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                                        ticDeviseLoading ? 'opacity-50 cursor-not-allowed' : ''
                                                    }`}
                                                >
                                                    {ticDeviseLoading ? (
                                                        <>
                                                            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                                            Import...
                                                        </>
                                                    ) : (
                                                        <>
                                                            <Upload className="w-4 h-4 mr-2" />
                                                            Importer
                                                        </>
                                                    )}
                                                </button>
                                                <input
                                                    type="file"
                                                    accept=".xlsx,.xls"
                                                    ref={fileInputRef}
                                                    onChange={handleFileChange}
                                                    style={{ display: 'none' }}
                                                />
                                            </div>
                                        </div>
                                        {importMessage && (
                                            <div className={`mt-4 p-3 rounded-xl border-2 flex items-center gap-2 ${
                                                importStatus === 'success'
                                                    ? 'border-green-300 bg-green-50'
                                                    : 'border-red-300 bg-red-50'
                                            }`}>
                                                {importStatus === 'success' ? (
                                                    <CheckCircle className="w-4 h-4 text-green-600" />
                                                ) : (
                                                    <AlertCircle className="w-4 h-4 text-red-600" />
                                                )}
                                                <div className={`text-sm font-medium ${
                                                    importStatus === 'success' ? 'text-green-800' : 'text-red-800'
                                                }`}>
                                                    {importMessage}
                                                </div>
                                            </div>
                                        )}
                                    </div>

                                    {/* Import Dérogation */}
                                    <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-xl p-6 border border-gray-200">
                                        <div className="flex items-center justify-between">
                                            <div className="flex items-center space-x-4">
                                                <div className="p-2 bg-white rounded-lg shadow-sm">
                                                    <FileText className="w-5 h-5 text-[#581D74]" />
                                                </div>
                                                <div>
                                                    <h3 className="font-semibold text-gray-900">Gestion des Dérogations</h3>
                                                    <p className="text-sm text-gray-600">Importer ou effacer les dérogations</p>
                                                </div>
                                            </div>
                                            <div className="flex items-center gap-4">
                                                <div className="relative">
                                                    <input
                                                        type="text"
                                                        readOnly
                                                        value={selectedDerroFile ? selectedDerroFile.name : "Aucun fichier sélectionné"}
                                                        onClick={() => document.getElementById('derro-file')?.click()}
                                                        className="w-80 px-4 py-3 border border-gray-300 rounded-xl bg-white cursor-pointer focus:outline-none focus:ring-2 focus:ring-[#581D74] hover:border-gray-400 transition-all duration-200 text-gray-600 font-medium"
                                                        placeholder="Sélectionner un fichier..."
                                                    />
                                                    <Upload className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5 pointer-events-none" />
                                                </div>
                                                <input
                                                    id="derro-file"
                                                    type="file"
                                                    accept=".xlsx,.xls"
                                                    onChange={(e) => {
                                                        if (e.target.files && e.target.files[0]) {
                                                            setSelectedDerroFile(e.target.files[0]);
                                                        }
                                                    }}
                                                    className="hidden"
                                                />
                                                <button
                                                    onClick={async () => {
                                                        setDeleteMessage('');
                                                        await handleImportDerrogation(
                                                            dates.dernierJour ? getNextDay(dates.dernierJour) : '',
                                                            selectedDerroFile ?? undefined
                                                        );
                                                    }}
                                                    disabled={isImportingDerro || !selectedDerroFile}
                                                    className={`flex items-center px-6 py-3 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                                        isImportingDerro || !selectedDerroFile ? 'opacity-50 cursor-not-allowed' : ''
                                                    }`}
                                                >
                                                    {isImportingDerro ? (
                                                        <>
                                                            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                                                            Import...
                                                        </>
                                                    ) : (
                                                        <>
                                                            <Upload className="w-4 h-4 mr-2" />
                                                            Importer
                                                        </>
                                                    )}
                                                </button>
                                                <button
                                                    onClick={async () => {
                                                        setDeleteMessage('');
                                                        setIsDeletingDerro(true);
                                                        try {
                                                            const res = await deleteDerogation();
                                                            setDeleteMessage(res.message);
                                                            setSelectedDerroFile(null);
                                                        } catch (e: any) {
                                                            setDeleteMessage(e.message);
                                                        } finally {
                                                            setIsDeletingDerro(false);
                                                        }
                                                    }}
                                                    disabled={isDeletingDerro}
                                                    className={`p-3 bg-red-500 hover:bg-red-600 text-white rounded-xl transition-all duration-200 shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                                        isDeletingDerro ? 'opacity-50 cursor-not-allowed' : ''
                                                    }`}
                                                    title="Supprimer les dérogations"
                                                >
                                                    {isDeletingDerro ? (
                                                        <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                                                    ) : (
                                                        <Trash className="w-5 h-5" />
                                                    )}
                                                </button>
                                            </div>
                                        </div>

                                        {/* Messages */}
                                        {(deleteMessage || derroMessage) && (
                                            <div className="mt-4">
                                                {deleteMessage ? (
                                                    <div className="p-3 rounded-xl border-2 border-red-300 bg-red-50 flex items-center gap-2">
                                                        <AlertCircle className="w-4 h-4 text-red-600" />
                                                        <div className="text-sm font-medium text-red-800">
                                                            {deleteMessage}
                                                        </div>
                                                    </div>
                                                ) : derroMessage ? (
                                                    <div className={`p-3 rounded-xl border-2 flex items-center gap-2 ${
                                                        derroStatus === 'success'
                                                            ? 'border-green-300 bg-green-50'
                                                            : 'border-red-300 bg-red-50'
                                                    }`}>
                                                        {derroStatus === 'success' ? (
                                                            <CheckCircle className="w-4 h-4 text-green-600" />
                                                        ) : (
                                                            <AlertCircle className="w-4 h-4 text-red-600" />
                                                        )}
                                                        <div className={`text-sm font-medium ${
                                                            derroStatus === 'success' ? 'text-green-800' : 'text-red-800'
                                                        }`}>
                                                            {derroMessage}
                                                        </div>
                                                    </div>
                                                ) : null}
                                            </div>
                                        )}
                                    </div>

                                    {/* Verification Button */}
                                    <div className="flex justify-center pt-4">
                                        <button
                                            onClick={handleVerification}
                                            disabled={isVerifying || verificationProgress.status === 'processing'}
                                            className={`flex items-center px-12 py-4 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl font-bold text-lg transition-all duration-200 shadow-xl hover:shadow-2xl transform hover:scale-105 ${
                                                isVerifying || verificationProgress.status === 'processing'
                                                    ? 'opacity-70 cursor-not-allowed'
                                                    : 'hover:from-[#4A1562] hover:to-[#581D74]'
                                            }`}
                                        >
                                            {(isVerifying || verificationProgress.status === 'processing') ? (
                                                <>
                                                    <div className="w-6 h-6 border-2 border-white border-t-transparent rounded-full animate-spin mr-3"></div>
                                                    VÉRIFICATION EN COURS...
                                                </>
                                            ) : (
                                                <>
                                                    VÉRIFICATION DES SOLDES
                                                </>
                                            )}
                                        </button>
                                    </div>

                                    {/* Error Message */}
                                    {verificationError && (
                                        <div className="mt-4 p-4 bg-red-50 border border-red-200 rounded-xl">
                                            <div className="flex items-center">
                                                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                                <p className="text-red-800 font-medium">{verificationError}</p>
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>

                        {/* Verification Results Section */}
                        {verificationResult && (
                            <>
                                <VerificationResults
                                    verificationResult={verificationResult}
                                    formatDateTime={formatDateTime}
                                    selectedExcelFile={selectedExcelFile}
                                    setSelectedExcelFile={setSelectedExcelFile}
                                    exportPdfFromExcel={exportPdfFromExcel}
                                    exportPdf={exportPdf}
                                    dates={dates}
                                    loadingExcel={loadingExcel}
                                    loadingAll={loadingAll}
                                    errorExcel={errorExcel}
                                    resultExcel={resultExcel}
                                    errorAll={errorAll}
                                    resultAll={resultAll}
                                    selectedEchelleExcelFile={selectedEchelleExcelFile}
                                    setSelectedEchelleExcelFile={setSelectedEchelleExcelFile}
                                    exportEchellePdfFromExcel={exportEchellePdfFromExcel}
                                    exportEchellePdf={exportEchellePdf}
                                    loadingEchelleExcel={loadingEchelleExcel}
                                    loadingEchelleAll={loadingEchelleAll}
                                    errorEchelleExcel={errorEchelleExcel}
                                    resultEchelleExcel={resultEchelleExcel}
                                    errorEchelleAll={errorEchelleAll}
                                    resultEchelleAll={resultEchelleAll}
                                />

                                <div className="flex justify-center">
                                    <button
                                        onClick={handleExportClick}
                                        disabled={loadingInjection}
                                        className={`flex items-center px-12 py-4 bg-gradient-to-r from-green-500 to-green-600 text-white rounded-xl font-bold text-lg transition-all duration-200 shadow-xl hover:shadow-2xl transform hover:scale-105 ${
                                            loadingInjection ? 'opacity-70 cursor-not-allowed' : 'hover:from-green-600 hover:to-green-700'
                                        }`}
                                    >
                                        {loadingInjection ? (
                                            <>
                                                <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-white mr-3"></div>
                                                Export en cours...
                                            </>
                                        ) : (
                                            <>
                                                <Download className="w-6 h-6 mr-3" />
                                                Exporter Injection
                                            </>
                                        )}
                                    </button>
                                </div>

                                {/* BMOI Popup */}
                                {showBMOIPopup && (
                                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                                        <div className="bg-white rounded-2xl p-8 shadow-2xl min-w-[400px] border border-gray-200">
                                            <div className="flex items-center space-x-3 mb-6">
                                                <div className="p-3 bg-gradient-to-br from-[#581D74] to-[#6B2C87] rounded-xl">
                                                    <Database className="w-6 h-6 text-white" />
                                                </div>
                                                <div>
                                                    <h3 className="text-xl font-bold text-gray-900">Identifiant BMOI</h3>
                                                    <p className="text-gray-600">Veuillez saisir l'identifiant requis</p>
                                                </div>
                                            </div>
                                            <input
                                                type="text"
                                                value={bmoiId}
                                                onChange={e => setBmoiId(e.target.value)}
                                                className="w-full px-4 py-4 border border-gray-300 rounded-xl mb-6 focus:ring-2 focus:ring-[#581D74] outline-none transition-all duration-200 font-medium"
                                                placeholder="Saisir l'identifiant BMOI"
                                            />
                                            <div className="flex justify-end gap-3">
                                                <button
                                                    onClick={() => setShowBMOIPopup(false)}
                                                    className="px-6 py-3 rounded-xl bg-gray-100 text-gray-700 font-semibold hover:bg-gray-200 transition-all duration-200"
                                                >
                                                    Annuler
                                                </button>
                                                <button
                                                    onClick={handleConfirmExport}
                                                    disabled={!bmoiId}
                                                    className={`px-6 py-3 rounded-xl bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white font-semibold transition-all duration-200 ${
                                                        !bmoiId ? 'opacity-50 cursor-not-allowed' : 'hover:from-[#4A1562] hover:to-[#581D74] shadow-lg hover:shadow-xl transform hover:scale-105'
                                                    }`}
                                                >
                                                    Valider
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                )}

                                {/* Export Messages */}
                                {(errorInjection || resultInjection) && (
                                    <div className="mt-4">
                                        {errorInjection && (
                                            <div className="p-4 bg-red-50 border border-red-200 rounded-xl">
                                                <div className="flex items-center">
                                                    <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                                    <p className="text-red-800 font-medium">{errorInjection}</p>
                                                </div>
                                            </div>
                                        )}
                                        {resultInjection && (
                                            <div className="p-4 bg-green-50 border border-green-200 rounded-xl">
                                                <div className="flex items-center">
                                                    <CheckCircle className="w-5 h-5 text-green-600 mr-2" />
                                                    <p className="text-green-800 font-medium">Export réussi</p>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                )}
                            </>
                        )}
                    </div>
                </main>

                <footer className="bg-white border-t border-gray-200 px-8 py-6 mt-12">
                    <div className="text-center">
                        <p className="text-gray-500 text-sm font-medium">
                            © 2025 BMOI - Tous droits réservés
                        </p>
                    </div>
                </footer>
            </div>
        </div>
    );
};

export default CalculArretes;