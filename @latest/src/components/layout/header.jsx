import logoImage from '../../assets/logo.png';
import '../../styles/layout/header.css'; // <-- Importa el CSS


export function Header() {
    return (
        <>
            <header className="header">
                <img className="logo" src={logoImage} alt="Logo" />
            </header>
        </>
    )
}