import Navbar from "./Navbar";

function ProtectedLayout({children}){
    return(
        <div className="h-screen w-screen flex flex-col bg-gray-100">
            <Navbar />
            <div className="flex-grow">{children}</div>
        </div>
    );
}

export default ProtectedLayout;