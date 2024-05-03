import { useEffect, useState } from 'react';
import moment from "moment";
import './EurojackpotResults.css';

const apiUrl = process.env.REACT_APP_API_BASE_URL;
const emptyFormData = { number: "", startDate: "", finishDate: "", winningNumber: "", includeSup: false }

/*
  This is the main component responsible for loeading the data from the backend, rendering and providing the options for filtering.
*/
export const EurojackpotResults = () => {
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [results, setResults] = useState([]);
    const [formData, setFormData] = useState(emptyFormData);

    // loads data from the API on first load and on search reset
    useEffect(() => {
        if (formData === emptyFormData) {
                setLoading(true);
                fetchResults();
        }
    }, [formData]);

    // constructs the request based on the search form data and updates the results
    const fetchResults = () => {
        setLoading(true);
        const { number, startDate, finishDate, winningNumber, includeSup } = formData;
        const fmtStartDate = startDate ? moment(startDate).format("DD.MM.yyyy") : "";
        const fmtFinishDate = finishDate ? moment(finishDate).format("DD.MM.yyyy") : "";

        fetch(`${apiUrl}/api/results?resultNumber=${number}&startDate=${fmtStartDate}&finishDate=${fmtFinishDate}&winningNumber=${winningNumber}&includeSupplementaryNumbers=${includeSup}`)
            .then(response => response.json())
            .then(data => {
                setResults(data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching results:', error);
                setLoading(false);
                setErrorMessage('Error obtaining results. Make sure the backend is running!');
            });
    }

    // update the form data on every component change
    const handleFormChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        setFormData({
            ...formData,
            [name]: value
        });
    }

    const handleSearch = (event) => {
        event.preventDefault();
        fetchResults();
    }

    const handleReset = (event) => {
        event.preventDefault();
        setFormData(emptyFormData);
    }

    if (loading) {
        return <p>Loading...</p>;
      }

    return (
        <div className="App">
            <h2>Eurojackpot Results</h2>
            <div className="container p-3">
            <h5>Filter results:</h5>
                        <form className="row align-items-center" onSubmit={handleSearch}>
                            <div className="col-auto">
                                <input type="number" placeholder="Result number" className="form-control" id="resultNumber" name="number" min="1" value={formData.number} onChange={handleFormChange} />
                            </div>
                            <div className="col-auto">
                                <label htmlFor="startDate" className="sr-only">Start date</label>
                            </div>
                            <div className="col-auto">
                                <input type="date" className="form-control" id="startDate" name="startDate" value={formData.startDate} max={formData.finishDate} onChange={handleFormChange} />
                            </div>
                            <div className="col-auto">
                                <label htmlFor="finishDate" className="sr-only">Finish date</label>
                            </div>
                            <div className="col-auto">
                                <input type="date" className="form-control" id="finishDate" name="finishDate" value={formData.finishDate} min={formData.startDate} onChange={handleFormChange} />
                            </div>
                            <div className="col-auto col-sm-2">
                                <input type="number" placeholder="Draw number" className="form-control" id="winningNumber" name="winningNumber" min="1" max="50" value={formData.winningNumber} onChange={handleFormChange} />
                            </div>
                            <div className="col-auto">
                                <div className="form-check">
                                    <input className="form-check-input" type="checkbox" id="includeSup" name="includeSup" disabled={!formData.winningNumber} checked={!!formData.winningNumber && formData.includeSup} onChange={handleFormChange} />
                                    <label className="form-check-label" htmlFor="includeSup">Include Supplementary numbers</label>
                                </div>
                            </div>
                            <div className="row gx-6 align-items-center justify-content-center">
                            <div className="col-auto p-3">
                                <button type="submit" className="btn btn-primary mb-2">Search</button>
                            </div>
                            <div className="col-auto">
                                <button className="btn btn-light mb-2" onClick={handleReset} >Reset</button>
                            </div>
                            </div>
                        </form>
    </div>
         {/* Conditional expression to display different messages based on error condition or results data */}
    <div>{ errorMessage ? <h4 className="error-message"> { errorMessage } </h4>
            : results.length === 0 ? <p><strong> No items match the criteria </strong></p>
            : <p><strong> Showing {results.length} item(s) </strong></p>  }</div>
            <table className="table table-dark table-striped table-bordered">
                <thead className="sticky-top">
                    <tr>
                        <th>Result number</th>
                        <th>Date</th>
                        <th>Draw #1</th>
                        <th>Draw #2</th>
                        <th>Draw #3</th>
                        <th>Draw #4</th>
                        <th>Draw #5</th>
                        <th>Sup. Draw #6</th>
                        <th>Sup. Draw #7</th>
                    </tr>
                </thead>
                <tbody>
                    {results.map((result, index) => (
                        <tr key={index}>
                            {Object.values(result).map((entry, columnIndex) => (
                                <td key={columnIndex}>{entry}</td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}
