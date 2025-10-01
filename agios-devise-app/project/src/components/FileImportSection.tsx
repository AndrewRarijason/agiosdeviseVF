import React, { RefObject } from 'react';
import { Database, Download, ChevronDown } from 'lucide-react';

interface FileImportSectionProps {
    openBksld: boolean;
    setOpenBksld: (v: boolean) => void;
    openBkdar: boolean;
    setOpenBkdar: (v: boolean) => void;
    openBkhis: boolean;
    setOpenBkhis: (v: boolean) => void;
    dropdownRefBksld: RefObject<HTMLDivElement>;
    dropdownRefBkdar: RefObject<HTMLDivElement>;
    dropdownRefBkhis: RefObject<HTMLDivElement>;
    isExportingBksldExcel: boolean;
    isExportingBksldCsv: boolean;
    handleExportBksldExcel: () => void;
    handleExportBksldCsv: () => void;
    progress: number;
    isExportingBkdarExcel: boolean;
    isExportingBkdarCsv: boolean;
    handleExportBkdarExcel: () => void;
    handleExportBkdarCsv: () => void;
    isExportingBkhisExcel: boolean;
    isExportingBkhisCsv: boolean;
    handleExportBkhisExcel: () => void;
    handleExportBkhisCsv: () => void;
}

const FileImportSection: React.FC<FileImportSectionProps> = ({
                                                                 openBksld, setOpenBksld, openBkdar, setOpenBkdar, openBkhis, setOpenBkhis,
                                                                 dropdownRefBksld, dropdownRefBkdar, dropdownRefBkhis,
                                                                 isExportingBksldExcel, isExportingBksldCsv, handleExportBksldExcel, handleExportBksldCsv,
                                                                 isExportingBkdarExcel, isExportingBkdarCsv, handleExportBkdarExcel, handleExportBkdarCsv,
                                                                 isExportingBkhisExcel, isExportingBkhisCsv, handleExportBkhisExcel, handleExportBkhisCsv,
                                                                 progress
                                                             }) => {
    const TableExtractionCard = ({
                                     tableName,
                                     description,
                                     isOpen,
                                     setIsOpen,
                                     dropdownRef,
                                     isExportingExcel,
                                     isExportingCsv,
                                     handleExportExcel,
                                     handleExportCsv,
                                     showProgress = false,
                                     progressValue = 0
                                 }: {
        tableName: string;
        description: string;
        isOpen: boolean;
        setIsOpen: (v: boolean) => void;
        dropdownRef: RefObject<HTMLDivElement>;
        isExportingExcel: boolean;
        isExportingCsv: boolean;
        handleExportExcel: () => void;
        handleExportCsv: () => void;
        showProgress?: boolean;
        progressValue?: number;
    }) => (
        <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-xl p-6 border border-gray-200">
            <div className="flex items-center justify-between">
                <div className="flex items-center space-x-4">
                    <div className="p-2 bg-white rounded-lg shadow-sm">
                        <Database className="w-5 h-5 text-[#581D74]" />
                    </div>
                    <div>
                        <h3 className="font-semibold text-gray-900">Table {tableName}</h3>
                        <p className="text-sm text-gray-600">{description}</p>
                    </div>
                </div>

                <div className="flex items-center gap-3">
                    {showProgress && (isExportingExcel || isExportingCsv) && (
                        <div className="flex items-center space-x-2">
                            <div className="w-32 bg-gray-200 rounded-full h-2">
                                <div
                                    className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] h-2 rounded-full transition-all duration-300"
                                    style={{ width: `${progressValue}%` }}
                                ></div>
                            </div>
                            <span className="text-sm font-medium text-gray-600">{progressValue}%</span>
                        </div>
                    )}

                    <div className="relative" ref={dropdownRef}>
                        <button
                            onClick={() => setIsOpen(!isOpen)}
                            disabled={isExportingExcel || isExportingCsv}
                            className={`flex items-center px-6 py-3 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                isExportingExcel || isExportingCsv ? 'opacity-50 cursor-not-allowed' : ''
                            }`}
                        >
                            {(isExportingExcel || isExportingCsv) ? (
                                <>
                                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin mr-2"></div>
                                    Extraction...
                                </>
                            ) : (
                                <>
                                    <Download className="w-4 h-4 mr-2" />
                                    Extraction
                                    <ChevronDown className={`w-4 h-4 ml-2 transition-transform duration-200 ${isOpen ? 'rotate-180' : ''}`} />
                                </>
                            )}
                        </button>

                        {isOpen && (
                            <div className="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-xl shadow-xl z-20 overflow-hidden">
                                <button
                                    onClick={() => {
                                        setIsOpen(false);
                                        handleExportExcel();
                                    }}
                                    disabled={isExportingExcel}
                                    className={`w-full text-left px-4 py-3 hover:bg-gray-50 transition-colors duration-200 flex items-center font-medium ${
                                        isExportingExcel ? 'opacity-60 cursor-not-allowed bg-gray-50' : ''
                                    }`}
                                >
                                    {isExportingExcel ? (
                                        <>
                                            <div className="w-4 h-4 border-2 border-[#581D74] border-t-transparent rounded-full animate-spin mr-2"></div>
                                            <span className="text-[#581D74]">Export Excel...</span>
                                        </>
                                    ) : (
                                        <>
                                            <div className="w-4 h-4 bg-green-500 rounded mr-2"></div>
                                            <span className="text-gray-700">Format XLSX</span>
                                        </>
                                    )}
                                </button>
                                <div className="border-t border-gray-100"></div>
                                <button
                                    onClick={() => {
                                        setIsOpen(false);
                                        handleExportCsv();
                                    }}
                                    disabled={isExportingCsv}
                                    className={`w-full text-left px-4 py-3 hover:bg-gray-50 transition-colors duration-200 flex items-center font-medium ${
                                        isExportingCsv ? 'opacity-60 cursor-not-allowed bg-gray-50' : ''
                                    }`}
                                >
                                    {isExportingCsv ? (
                                        <>
                                            <div className="w-4 h-4 border-2 border-[#581D74] border-t-transparent rounded-full animate-spin mr-2"></div>
                                            <span className="text-[#581D74]">Export CSV...</span>
                                        </>
                                    ) : (
                                        <>
                                            <div className="w-4 h-4 bg-blue-500 rounded mr-2"></div>
                                            <span className="text-gray-700">Format CSV</span>
                                        </>
                                    )}
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-8 border-b border-gray-200">
                <div className="flex items-center space-x-3 mb-8">
                    <div className="p-3 bg-gradient-to-br from-orange-500 to-orange-600 rounded-xl">
                        <Database className="w-6 h-6 text-white" />
                    </div>
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900">Extraction des Tables</h2>
                        <p className="text-gray-600 mt-1">Exportez les données des tables de base</p>
                    </div>
                </div>

                <div className="space-y-6">
                    <TableExtractionCard
                        tableName="BKSLD"
                        description="Extraction des données de soldes"
                        isOpen={openBksld}
                        setIsOpen={setOpenBksld}
                        dropdownRef={dropdownRefBksld}
                        isExportingExcel={isExportingBksldExcel}
                        isExportingCsv={isExportingBksldCsv}
                        handleExportExcel={handleExportBksldExcel}
                        handleExportCsv={handleExportBksldCsv}
                        showProgress={true}
                        progressValue={progress}
                    />

                    <TableExtractionCard
                        tableName="BKDAR"
                        description="Extraction des données d'arrêtés"
                        isOpen={openBkdar}
                        setIsOpen={setOpenBkdar}
                        dropdownRef={dropdownRefBkdar}
                        isExportingExcel={isExportingBkdarExcel}
                        isExportingCsv={isExportingBkdarCsv}
                        handleExportExcel={handleExportBkdarExcel}
                        handleExportCsv={handleExportBkdarCsv}
                    />

                    <TableExtractionCard
                        tableName="BKHIS"
                        description="Extraction des données historiques"
                        isOpen={openBkhis}
                        setIsOpen={setOpenBkhis}
                        dropdownRef={dropdownRefBkhis}
                        isExportingExcel={isExportingBkhisExcel}
                        isExportingCsv={isExportingBkhisCsv}
                        handleExportExcel={handleExportBkhisExcel}
                        handleExportCsv={handleExportBkhisCsv}
                    />
                </div>
            </div>
        </div>
    );
};

export default FileImportSection;