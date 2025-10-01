const CustomTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
        const value = payload[0].value;
        return (
            <div className="bg-white p-2 rounded shadow text-[#581D74] font-semibold">
                Agios : {value.toLocaleString('fr-FR', { minimumFractionDigits: 2 })}
            </div>
        );
    }
    return null;
};

export { CustomTooltip };