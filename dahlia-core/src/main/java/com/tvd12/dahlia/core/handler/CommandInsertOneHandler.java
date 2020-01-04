package com.tvd12.dahlia.core.handler;

import java.util.UUID;

import com.tvd12.dahlia.core.command.InsertOne;
import com.tvd12.dahlia.core.constant.Constants;
import com.tvd12.dahlia.core.entity.Collection;
import com.tvd12.dahlia.core.entity.Record;
import com.tvd12.dahlia.core.exception.DuplicatedIdException;
import com.tvd12.dahlia.core.setting.CollectionSetting;
import com.tvd12.dahlia.core.storage.CollectionStorage;
import com.tvd12.ezyfox.entity.EzyObject;

public class CommandInsertOneHandler extends CommandAbstractHandler<InsertOne> {

	@Override
	public Object handle(InsertOne command) {
		int collectionId = command.getCollectionId();
		EzyObject data = command.getData();
		
		Collection collection = databases.getCollection(collectionId);
		CollectionSetting setting = collection.getSetting();
		CollectionStorage collectionStorage = storage.getCollectionStorage(collectionId);

		Comparable id = data.get(Constants.FIELD_ID);
		synchronized (collection) {
			if(id != null) {
				Record existed = collection.findById(id);
				if(existed != null)
					throw new DuplicatedIdException(collection.getName(), id);
			}
			else {
				while(true) {
					id = UUID.randomUUID();
					Record existed = collection.findById(id);
					if(existed == null)
						break;
				}
				data.put(Constants.FIELD_ID, id);
			}
			Record record = new Record(id, collection.getDataSize());
			collection.insert(record);
			collection.increaseDataSize();
			collectionStorage.storeRecord(record, setting.getId(), setting.getFields(), data);
		}
		return data;
	}
	
}
