package com.yrgo.services.diary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DiaryManagementServiceMockImpl implements DiaryManagementService {
	
	private final Set<Action> allActions;

	public DiaryManagementServiceMockImpl() {
		this.allActions = new HashSet<>();
	}

	@Override
	public void recordAction(Action action) {
		allActions.add(action);
	}

	public List<Action> getAllIncompleteActions(String requiredUser) {
		var incompleteActions = new ArrayList<Action>();
		for (var action : allActions) {
			if(action.getOwningUser().equals(requiredUser) && !action.isComplete()) {
				incompleteActions.add(action);
			}
		}
		return incompleteActions;
	}

}
