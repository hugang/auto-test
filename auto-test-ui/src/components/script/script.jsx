
import { useState, useEffect } from 'react';
import axios from 'axios';
import { API_URL } from "../../util/Constants.jsx";

function Script() {
    const [scripts, setScripts] = useState([]);
    const [scriptName, setScriptName] = useState('');

    useEffect(() => {
        axios.get(API_URL + '/scripts')
            .then(response => {
                setScripts(response.data);
            })
            .catch(error => {
                console.error(error);
            });
    }, []);

    const handleScriptNameChange = (event) => {
        setScriptName(event.target.value);
    };

    const handleCreateScript = () => {
        axios.post(API_URL + '/scripts', { scriptName })
            .then(response => {
                setScripts([...scripts, response.data]);
                setScriptName('');
            })
            .catch(error => {
                console.error(error);
            });
    };

    const handleDeleteScript = (id) => {
        axios.delete(API_URL + `/scripts/${id}`)
            .then(() => {
                setScripts(scripts.filter(script => script.id !== id));
            })
            .catch(error => {
                console.error(error);
            });
    };

    const handleExecuteScript = (id) => {
        axios.post(API_URL + `/scripts/execute/${id}`)
            .then(() => {
                console.log('executed');
            })
            .catch(error => {
                console.error(error);
            });
    };
    
    return (
        <div>
            <ul>
                {scripts.map(script => (
                    <li key={script.id}>
                        <a href={`/command/${script.id}`}>{script.scriptName}</a>
                        <button onClick={() => handleDeleteScript(script.id)}>Delete</button>
                        <button onClick={() => handleExecuteScript(script.id)}>execute</button>
                    </li>
                ))}
            </ul>
            <div>
                <input type="text" value={scriptName} onChange={handleScriptNameChange} />
                <button onClick={handleCreateScript}>Create</button>

            </div>
        </div>
    );
}

export default Script;