import { Container } from 'react-bootstrap';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Script from './components/Script';
import Command from './components/Command';

function App() {
  return (
    <Container>
      <BrowserRouter>
        <div><Routes>
          <Route path="/" element={<Script />} />
          <Route path="/script" element={<Script />} />
          <Route path="/command/:scriptId" element={<Command />} />
        </Routes>
        </div>
      </BrowserRouter>

    </Container>
  );
}

export default App;
