import { createStore,combineReducers } from 'redux'
import caseAction from '../actions/actions'


let store = createStore(caseAction);

export default store;
