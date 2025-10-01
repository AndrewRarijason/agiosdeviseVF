import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage, Accueil, GestionUser } from './components';
import CalculArretes from './pages/CalculArretes';
import PrivateRoute from './components/PrivateRoute';
import EditUser from "./components/EditUser.tsx";
import GestionTicDevise from "./pages/GestionTicDevise.tsx";
import HistoriqueArretes from "./pages/HistoriqueArretes.tsx";
import HistoriqueArreteDetail from "./pages/HistoriqueArreteDetail.tsx";
import DashboardResultat from "./components/DashboardResultat.tsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/accueil" element={
                    <PrivateRoute><Accueil /></PrivateRoute>
                } />
                <Route path="/calcul-arretes" element={
                    <PrivateRoute excludedRole="Consultant">
                        <CalculArretes />
                    </PrivateRoute>
                } />
                <Route path="/gestion-utilisateurs" element={
                    <PrivateRoute requiredRole="Admin">
                        <GestionUser />
                    </PrivateRoute>
                } />
                <Route path="/historique-arrete" element={
                    <PrivateRoute>
                        <HistoriqueArretes />
                    </PrivateRoute>
                } />
                <Route path="/arretes/historique/detail" element={
                    <PrivateRoute>
                        <HistoriqueArreteDetail/>
                    </PrivateRoute>
                } />
                <Route path="/users/edit/:id" element={
                    <PrivateRoute requiredRole="Admin">
                        <EditUser />
                    </PrivateRoute>
                } />
                <Route path="/gestion-ticdevise" element={
                    <PrivateRoute requiredRole="Admin">
                        <GestionTicDevise />
                    </PrivateRoute>
                } />
                <Route path="/dashboard" element={
                    <PrivateRoute>
                        <DashboardResultat/>
                    </PrivateRoute>
                } />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </Router>
    );
}

export default App;