import logoImage from '../../assets/logo.png';
import '../../styles/layout/header.css';


export function Header() {
    return (
        <>
            <header className="header">
                <img className="logo" src={logoImage} alt="Logo" />
            </header>
        </>
    )
}